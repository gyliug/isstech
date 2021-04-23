import cn.hutool.core.util.StrUtil;
import com.entfrm.biz.intelligent.service.FormExtendDesignService;
import com.entfrm.web.WebApplication;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.FlowNode;
import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricProcessInstanceQuery;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.image.impl.DefaultProcessDiagramGenerator;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

/**
 * <p>
 * 单元测试
 * </p>
 *
 * @Author: anonymous
 * @Date: 2021/2/9
 */
@RunWith(SpringJUnit4ClassRunner.class)//初始化spring上下文
@SpringBootTest(classes = WebApplication.class,webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WebApplicationTest {

    @Autowired
    FormExtendDesignService formExtendDesignService;

    @Autowired
    HistoryService historyService;

    @Test
    @Transactional
    @Rollback(false)
    public void demo() throws Exception {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        System.out.println(processEngine);
    }


    @Test
    @Transactional
    @Rollback(false)
    public void TestActivitiHistory(){

        HistoricProcessInstanceQuery historicProcessInstanceQuery=historyService.createHistoricProcessInstanceQuery();
        historicProcessInstanceQuery.orderByProcessInstanceStartTime().asc();
        List<HistoricProcessInstance> historicActivityInstances=historicProcessInstanceQuery.list();

        for(HistoricProcessInstance hi:historicActivityInstances){
            System.out.println(hi.getDeleteReason());
            System.out.println("<==========================>");
        }

    }














    @Resource
    RepositoryService repositoryService;


    @Test
    public void displayFlowCurrPicnew() {

        BufferedImage img = new BufferedImage(300, 150, BufferedImage.TYPE_INT_RGB);

        // 组织流程表中的字段中的值
        //流程图对象Key      表Act_Ru_EXECUTION 列BUSINESS_KEY_）
        String objId = "PackageBean.DB20190128";
        try {
            // 获取流程图数据
            HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
                    .processInstanceBusinessKey(objId).singleResult();

            // 获取所有节点
            List<HistoricActivityInstance> historicActivityInstanceLinkList = historyService
                    .createHistoricActivityInstanceQuery().processInstanceId(historicProcessInstance.getId()).list();

            // 高亮环节id集合
            ProcessDefinitionEntity definitionEntity = (ProcessDefinitionEntity) repositoryService
                    .getProcessDefinition(historicProcessInstance.getProcessDefinitionId());

            // 得到所有进程线
            List<String> highLightedFlowsList = getHighLightedFlows(definitionEntity, historicActivityInstanceLinkList);

            // 获取流程图对象 使用部署对象ID查询
            ProcessDefinition define = repositoryService.createProcessDefinitionQuery()
                    .deploymentId(definitionEntity.getDeploymentId()).list().get(0);

            // 获取历史节点不包括分叉节点
            // List<HistoricTaskInstance> htiList =
            // historyService.createHistoricTaskInstanceQuery()
            // .processInstanceBusinessKey(objId).orderByTaskId().asc().list();

            List<String> highLines = new ArrayList<String>();// 高亮线集合
            highLines.addAll(highLightedFlowsList);

            List<String> highNodes = new ArrayList<String>();// 高亮节点

            // 获取流程图model
            BpmnModel bpmnModel = repositoryService.getBpmnModel(define.getId());

            // Collections.reverse(htiList);// list倒序
            for (int i = 0; i < historicActivityInstanceLinkList.size(); i++) {
                // 节点高亮
                highNodes.add(historicActivityInstanceLinkList.get(i).getActivityId());


                // 根据节点获得对象
                // 获取与 当前节点 出去关连的节点是否是结束节点如果是结束节点进行高亮
                FlowNode fl = (FlowNode) bpmnModel
                        .getFlowElement(historicActivityInstanceLinkList.get(i).getActivityId());
                List<SequenceFlow> sequenceFlows = fl.getOutgoingFlows();
                for (SequenceFlow sequenceFlow : sequenceFlows) {
                    if (sequenceFlow.getTargetRef().contains("endevent")) {
                        highNodes.add(sequenceFlow.getTargetRef());
                        highLines.add(sequenceFlow.getId());
                    }
                }

            }

            // 递归获取节点
            // highNodes.addAll(getHighNodes(highNodes,bpmnModel,task.getTaskDefinitionKey()));
            // highLines.addAll(getHighLines(highLines,bpmnModel,task.getTaskDefinitionKey()));

            // 获取流程图图像字符流
            DefaultProcessDiagramGenerator generator = new DefaultProcessDiagramGenerator();

            // 流程图输出
            // 参数说明：流程图模板、输出图片类型、高亮节点、高亮线、设置字体、设置字体、null、生成版本 微软雅黑
            InputStream inputStream = generator.generateDiagram(
                    bpmnModel,
                    "jpg",
                    highNodes,
                    highLines,
                    "黑体",
                    "黑体",
                    "黑体",
                    null,
                    2.0
            );

            //不输出图片可以直接调用 以资源文件的方式传输到前台
            //this.writeFile(respon,inputStream );

            byte[] bytes = IOUtils.toByteArray(inputStream);
            String encoded = Base64.getEncoder().encodeToString(bytes);

            // 输出资源内容到相应对象
            byte[] b = new byte[1024];
            File file = new File("D://1.jpg");
            int len;

            OutputStream out = new FileOutputStream(file);
            // imageStream=imageStream.

            while ((len = inputStream.read(b, 0, 1024)) != -1) {
                out.write(b, 0, len);
            }

            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取高亮的线
     *
     * @param bpmnModel
     * @param key
     * @return
     *
     */
    public List<String> getHighLines(List<String> highLines, BpmnModel bpmnModel, String key, String key2) {
        FlowNode fl = (FlowNode) bpmnModel.getFlowElement(key);
        // List<SequenceFlow> inFlows = fl.getIncomingFlows();

        List<SequenceFlow> outFlows = fl.getOutgoingFlows();
        for (SequenceFlow sequenceFlow : outFlows) {

            highLines.add(sequenceFlow.getId());
        }

        return highLines;
    }

    /**
     * 获取高亮的线
     *
     * @return
     *
     */
    private List<String> getHighLightedFlows(ProcessDefinitionEntity processDefinitionEntity,
                                             List<HistoricActivityInstance> historicActivityInstances) {
        List<String> highFlows = new ArrayList<String>();// 用以保存高亮的线flowId
        for (int i = 0; i < historicActivityInstances.size() - 1; i++) {// 对历史流程节点进行遍历
            ActivityImpl activityImpl = processDefinitionEntity
                    .findActivity(historicActivityInstances.get(i).getActivityId());// 得到节点定义的详细信息
            List<ActivityImpl> sameStartTimeNodes = new ArrayList<ActivityImpl>();// 用以保存后需开始时间相同的节点
            ActivityImpl sameActivityImpl1 = processDefinitionEntity
                    .findActivity(historicActivityInstances.get(i + 1).getActivityId());
            // 将后面第一个节点放在时间相同节点的集合里
            sameStartTimeNodes.add(sameActivityImpl1);
            for (int j = i + 1; j < historicActivityInstances.size() - 1; j++) {
                HistoricActivityInstance activityImpl1 = historicActivityInstances.get(j);// 后续第一个节点
                HistoricActivityInstance activityImpl2 = historicActivityInstances.get(j + 1);// 后续第二个节点
                if (activityImpl1.getStartTime().equals(activityImpl2.getStartTime())) {
                    // 如果第一个节点和第二个节点开始时间相同保存
                    ActivityImpl sameActivityImpl2 = processDefinitionEntity
                            .findActivity(activityImpl2.getActivityId());
                    sameStartTimeNodes.add(sameActivityImpl2);
                } else {
                    // 有不相同跳出循环
                    break;
                }
            }
            List<PvmTransition> pvmTransitions = activityImpl.getOutgoingTransitions();// 取出节点的所有出去的线
            for (PvmTransition pvmTransition : pvmTransitions) {
                // 对所有的线进行遍历
                ActivityImpl pvmActivityImpl = (ActivityImpl) pvmTransition.getDestination();
                // 如果取出的线的目标节点存在时间相同的节点里，保存该线的id，进行高亮显示
                if (sameStartTimeNodes.contains(pvmActivityImpl)) {
                    highFlows.add(pvmTransition.getId());
                }
            }
        }
        return highFlows;
    }

    /**
     * public List<String> getHighLines(List<String> highLines, BpmnModel bpmnModel,
     * String key) { FlowNode fl = (FlowNode) bpmnModel.getFlowElement(key);
     * List<SequenceFlow> sequenceFlows = fl.getIncomingFlows(); for (SequenceFlow
     * sf : sequenceFlows) { if (StringUtils.isNotBlank(sf.getSourceRef()) &&
     * !highLines.contains(sf.getSourceRef()) &&
     * !"usertask2".equals(sf.getSourceRef())) {
     *
     * highLines.add(sf.getId()); getHighLines(highLines, bpmnModel,
     * sf.getSourceRef());
     *
     *
     * if (highLine != null && highLine.size() > 0) { highLines.addAll(highLine); }
     *
     *
     * } } return highLines; }
     */

    /**
     * 获取高亮的节点
     *
     * @param bpmnModel
     * @param key
     * @return
     */
    private List<String> getHighNodes(List<String> highNodes, BpmnModel bpmnModel, String key) {

        FlowNode fl = (FlowNode) bpmnModel.getFlowElement(key);
        List<SequenceFlow> sequenceFlows = fl.getIncomingFlows();

        highNodes.add(key);
        for (SequenceFlow sf : sequenceFlows) {
            if (StrUtil.isNotBlank(sf.getSourceRef()) && !highNodes.contains(sf.getSourceRef())
                    && !"usertask2".equals(sf.getSourceRef())) {
                List<String> HighNode = getHighNodes(highNodes, bpmnModel, sf.getSourceRef());
                if (HighNode != null && HighNode.size() > 0) {
                    highNodes.addAll(HighNode);
                }

            }
        }
        return highNodes;
    }

    // 将图片输入流输出到页面
    public void writeFile(HttpServletResponse resp, InputStream inputStream) {
        OutputStream out = null;
        try {
            out = resp.getOutputStream();
            int len = 0;
            byte[] b = new byte[1024];
            while ((len = inputStream.read(b)) != -1) {
                out.write(b, 0, len);
            }
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
















}
