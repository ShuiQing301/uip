package com.sq.protocol.opc.service;

import com.sq.inject.annotation.BaseComponent;
import com.sq.protocol.opc.component.OpcRegisterFactory;
import com.sq.protocol.opc.component.UtgardOpcHelper;
import com.sq.protocol.opc.domain.MesuringPoint;
import com.sq.protocol.opc.domain.OpcServerInfomation;
import com.sq.protocol.opc.domain.OriginalData;
import com.sq.protocol.opc.domain.ScreenInfo;
import com.sq.protocol.opc.repository.ScreenInfoRepository;
import com.sq.service.BaseService;
import org.jinterop.dcom.common.JIException;
import org.openscada.opc.lib.common.NotConnectedException;
import org.openscada.opc.lib.da.*;
import org.openscada.opc.lib.da.browser.Leaf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.net.UnknownHostException;
import java.text.DecimalFormat;
import java.util.*;

/**
 * 第三方数据推送服务.
 * User: shuiqing
 * Date: 2015/7/6
 * Time: 19:17
 * Email: shuiqing301@gmail.com
 * GitHub: https://github.com/ShuiQing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
@Service
public class PushDataThirdService extends BaseService<MesuringPoint, Long> {

    private static final Logger log = LoggerFactory.getLogger(PushDataThirdService.class);

    private static final int DCS_CID = 1;

    @Autowired
    @BaseComponent
    private ScreenInfoRepository screenInfoRepository;

    DecimalFormat format = new DecimalFormat("0.00");

    /**
     * 大屏数据推送
     */
    public void screenDataPush () {
        Server server = UtgardOpcHelper.connect(DCS_CID);
        Collection<Leaf> leafs = new LinkedList<Leaf>();
        List<ScreenPushData> screenPushDataList = fillCodeList();
        for (ScreenPushData screenPushData:screenPushDataList) {
            Leaf leaf = new Leaf(null, screenPushData.getItemCode(), screenPushData.getItemCode());
            leafs.add(leaf);
        }
        Group group = null;
        final Item[] itemArr = new Item[leafs.size()];
        int item_flag = 0;
        try {
            group = server.addGroup();
            for(Leaf leaf:leafs){
                log.error("code->" + leaf.getItemId());
                Item item = group.addItem(leaf.getItemId());
                itemArr[item_flag] = item;
                item_flag++;
            }
            Map<Item, ItemState> syncItems = null;
            /** arg1 false 读取缓存数据 OPCDATASOURCE.OPC_DS_CACHE  */
            syncItems = group.read(false, itemArr);
            List<ScreenInfo> screenInfoList = screenInfoRepository.findAll();
            for (Map.Entry<Item, ItemState> entry : syncItems.entrySet()) {
                log.error("key= " + entry.getKey().getId()
                        + " and value= " + entry.getValue().getValue().toString());
                String itemValue = entry.getValue().getValue().toString();
                for (ScreenPushData screenPushData:screenPushDataList) {
                    if (screenPushData.getItemCode().equals(entry.getKey().getId())) {
                        screenPushData.setItemValue(
                                format.format(new BigDecimal(itemValue.substring(2, itemValue.length() - 2))));
                    }
                }
            }

            pushScreenDataSync(screenInfoList,screenPushDataList);
            screenInfoRepository.save(screenInfoList);
        } catch (UnknownHostException e) {
            log.error("Host unknow error.",e);
        } catch (NotConnectedException e) {
            log.error("Connnect to opc error.", e);
        } catch (JIException e) {
            log.error("Opc server connect error.", e);
        } catch (DuplicateGroupException e) {
            log.error("Group duplicate error.", e);
        } catch (AddFailedException e) {
            log.error("Group add error.", e);
        }
    }

    /**
     * 组织大屏显示需要的测点列表
     * @return
     */
    public List<ScreenPushData> fillCodeList () {
        List<ScreenPushData> screenPushDataList = new ArrayList<ScreenPushData>();

        //DUTY
        screenPushDataList.add(generateScreenPushData("DUTY->1->Macsv5.Device3.Group3.B1ZQCHBY04"));
        screenPushDataList.add(generateScreenPushData("DUTY->2->Macsv5.Device3.Group3.NET1120"));
        screenPushDataList.add(generateScreenPushData("DUTY->3->Macsv5.Device3.Group3.NET1220"));
        screenPushDataList.add(generateScreenPushData("DUTY->4->Macsv5.Device3.Group3.NET1320"));

        //HCL
        screenPushDataList.add(generateScreenPushData("HCL->1->Macsv5.Device3.Group3.B1ZQCHAI07R"));
        screenPushDataList.add(generateScreenPushData("HCL->2->Macsv5.Device3.Group3.B2ZQCHBY05"));
        screenPushDataList.add(generateScreenPushData("HCL->3->Macsv5.Device3.Group3.B3ZQCHBY05"));
        screenPushDataList.add(generateScreenPushData("HCL->4->Macsv5.Device3.Group3.B4ZQCHBY05"));

        //CO
        screenPushDataList.add(generateScreenPushData("CO->1->Macsv5.Device3.Group3.B1CEMS_CO"));
        screenPushDataList.add(generateScreenPushData("CO->2->Macsv5.Device3.Group3.B2CEMS_CO"));
        screenPushDataList.add(generateScreenPushData("CO->3->Macsv5.Device3.Group3.B3CEMS_CO"));
        screenPushDataList.add(generateScreenPushData("CO->4->Macsv5.Device3.Group3.B4CEMS_CO"));

        //SO2
        screenPushDataList.add(generateScreenPushData("SO2->1->Macsv5.Device3.Group3.B1ZQCHBY03"));
        screenPushDataList.add(generateScreenPushData("SO2->2->Macsv5.Device3.Group3.NET1119"));
        screenPushDataList.add(generateScreenPushData("SO2->3->Macsv5.Device3.Group3.NET1219"));
        screenPushDataList.add(generateScreenPushData("SO2->4->Macsv5.Device3.Group3.NET1319"));

        //NOX
        screenPushDataList.add(generateScreenPushData("NOX->1->Macsv5.Device3.Group3.B1ZQCHBY02"));
        screenPushDataList.add(generateScreenPushData("NOX->2->Macsv5.Device3.Group3.NET1118"));
        screenPushDataList.add(generateScreenPushData("NOX->3->Macsv5.Device3.Group3.NET1218"));
        screenPushDataList.add(generateScreenPushData("NOX->4->Macsv5.Device3.Group3.NET1318"));

        //炉温
        screenPushDataList.add(generateScreenPushData("LUWEN->1->Macsv5.Device3.Group3.B1MNAI033"));
        screenPushDataList.add(generateScreenPushData("LUWEN->2->Macsv5.Device3.Group3.B2MNAI033"));
        screenPushDataList.add(generateScreenPushData("LUWEN->3->Macsv5.Device3.Group3.B3MNAI033"));
        screenPushDataList.add(generateScreenPushData("LUWEN->4->Macsv5.Device3.Group3.B4MNAI033"));

        //炉温
        screenPushDataList.add(generateScreenPushData("HF->1->Macsv5.Device3.Group3.B1CEMS_HF"));
        screenPushDataList.add(generateScreenPushData("HF->2->Macsv5.Device3.Group3.B2CEMS_HF"));
        screenPushDataList.add(generateScreenPushData("HF->3->Macsv5.Device3.Group3.B3CEMS_HF"));
        screenPushDataList.add(generateScreenPushData("HF->4->Macsv5.Device3.Group3.B4CEMS_HF"));

        return screenPushDataList;
    }

    /**
     * 根据字符串生成点值对象
     * @param screenCode
     * @return
     */
    private ScreenPushData generateScreenPushData (String screenCode) {
        String[] strArr = screenCode.split("->");
        ScreenPushData screenPushData = new ScreenPushData();
        screenPushData.setGroup(strArr[0]);
        screenPushData.setSerialNo(Integer.parseInt(strArr[1]));
        screenPushData.setItemCode(strArr[2]);
        log.error("group->" + strArr[0] + ";serialNo->" + strArr[1] + ";itemCode->" + strArr[2]);
        return screenPushData;
    }

    /**
     * 同步大屏数据
     * @param screenInfoList
     * @param screenPushDataList
     * @return
     */
    private List<ScreenInfo> pushScreenDataSync(List<ScreenInfo> screenInfoList,List<ScreenPushData> screenPushDataList) {
        for (ScreenPushData screenPushData:screenPushDataList) {
            for (ScreenInfo screenInfo:screenInfoList) {
                if (screenInfo.getBianma().equals(screenPushData.getGroup())) {
                    if (screenPushData.getSerialNo() == 1) {
                        screenInfo.setGuolu1(screenPushData.getItemValue());
                    } else if (screenPushData.getSerialNo() == 2) {
                        screenInfo.setGuolu2(screenPushData.getItemValue());
                    } else if (screenPushData.getSerialNo() == 3) {
                        screenInfo.setGuolu3(screenPushData.getItemValue());
                    } else if (screenPushData.getSerialNo() == 4) {
                        screenInfo.setGuolu4(screenPushData.getItemValue());
                    }
                }
            }
        }

        return screenInfoList;
    }

    /**
     * 测点和测值的对应关系
     */
    class ScreenPushData{

        private String group;

        private String itemCode;

        private String itemValue;

        private Integer serialNo;

        public String getGroup() {
            return group;
        }

        public void setGroup(String group) {
            this.group = group;
        }

        public String getItemCode() {
            return itemCode;
        }

        public void setItemCode(String itemCode) {
            this.itemCode = itemCode;
        }

        public String getItemValue() {
            return itemValue;
        }

        public void setItemValue(String itemValue) {
            this.itemValue = itemValue;
        }

        public Integer getSerialNo() {
            return serialNo;
        }

        public void setSerialNo(Integer serialNo) {
            this.serialNo = serialNo;
        }
    }
}
