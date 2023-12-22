export default (key, name, type) => {
    if (!type) type = 'camunda';
    const TYPE_TARGET = {
        activiti: 'http://activiti.org/bpmn',
        camunda: 'http://bpmn.io/schema/bpmn',
        flowable: 'http://flowable.org/bpmn'
    };
    return `<?xml version="1.0" encoding="UTF-8"?>
<bpmn2:definitions xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:bpmn2="http://www.omg.org/spec/BPMN/20100524/MODEL" 
xmlns:bpmndi="http://www.omg.org/spec/BPMN/20100524/DI" xmlns:dc="http://www.omg.org/spec/DD/20100524/DC" 
xmlns:flowable="http://flowable.org/bpmn" id="diagram_${key}" targetNamespace="${TYPE_TARGET[type]}">
  <bpmn2:process id="${key}" name="${name}" isExecutable="true">
    <bpmn2:startEvent id="Event_0r3d7ix" />
  </bpmn2:process>
  <bpmndi:BPMNDiagram id="BPMNDiagram_1">
    <bpmndi:BPMNPlane id="BPMNPlane_1" bpmnElement="${key}">
      <bpmndi:BPMNShape id="Event_0r3d7ix_di" bpmnElement="Event_0r3d7ix">
        <dc:Bounds x="372" y="182" width="36" height="36" />
      </bpmndi:BPMNShape>
    </bpmndi:BPMNPlane>
  </bpmndi:BPMNDiagram>
</bpmn2:definitions>`;
};
