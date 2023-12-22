export namespace Task {
  export interface FlowableTaskDto {
    /* */
    taskId?: string;

    /* */
    taskName?: string;

    /* */
    userId?: string;

    /* */
    comment?: string;

    /* */
    procInsId?: string;

    /* */
    targetKey?: string;

    /* */
    variables?: Record<string, unknown>;

    /* */
    assignee?: string;

    /* */
    candidateUsers?: Record<string, unknown>[];

    /* */
    candidateGroups?: Record<string, unknown>[];

    /* */
    copyUserIds?: Record<string, unknown>[];

    /* */
    nextUserIds?: Record<string, unknown>[];
  }

  export interface Value {}

  export interface AdditionalProperties1 {
    name: string;
    value: string;
    namespacePrefix: string;
    namespace: string;
    values: Value;
  }

  export interface Attribute {
    additionalProperties1: AdditionalProperties1[];
  }

  export interface Value {}

  export interface AdditionalProperties1 {
    id: string;
    xmlRowNumber: number;
    xmlColumnNumber: number;
    attributes: Attribute;
    name: string;
    namespacePrefix: string;
    namespace: string;
    elementText: string;
    values: Value;
  }

  export interface ExtensionElement {
    additionalProperties1: AdditionalProperties1[];
  }

  export interface Attribute {}

  export interface ExtensionElement {}

  export interface Attribute {}

  export interface ExtensionElement {}

  export interface Attribute {}

  export interface ExtensionElement {}

  export interface Attribute {}

  export interface Value {}

  export interface Value {
    id: string;
    xmlRowNumber: number;
    xmlColumnNumber: number;
    extensionElements: ExtensionElement;
    attributes: Attribute;
    fieldName: string;
    stringValue: string;
    expression: string;
    values: Value;
  }

  export interface FieldExtension {
    id: string;
    xmlRowNumber: number;
    xmlColumnNumber: number;
    extensionElements: ExtensionElement;
    attributes: Attribute;
    fieldName: string;
    stringValue: string;
    expression: string;
    values: Value;
  }

  export interface ExtensionElement {}

  export interface Attribute {}

  export interface ExtensionElement {}

  export interface Attribute {}

  export interface ExtensionElement {}

  export interface Attribute {}

  export interface Value {}

  export interface Value {
    id: string;
    xmlRowNumber: number;
    xmlColumnNumber: number;
    extensionElements: ExtensionElement;
    attributes: Attribute;
    values: Value;
  }

  export interface Value {
    id: string;
    xmlRowNumber: number;
    xmlColumnNumber: number;
    extensionElements: ExtensionElement;
    attributes: Attribute;
    values: Value;
  }

  export interface ScriptInfo {
    id: string;
    xmlRowNumber: number;
    xmlColumnNumber: number;
    extensionElements: ExtensionElement;
    attributes: Attribute;
    language: string;
    resultVariable: string;
    script: string;
    values: Value;
  }

  export interface Value {}

  export interface ExecutionListener {
    id: string;
    xmlRowNumber: number;
    xmlColumnNumber: number;
    extensionElements: ExtensionElement;
    attributes: Attribute;
    event: string;
    implementationType: string;
    implementation: string;
    fieldExtensions: FieldExtension[];
    onTransaction: string;
    customPropertiesResolverImplementationType: string;
    customPropertiesResolverImplementation: string;
    scriptInfo: ScriptInfo;
    values: Value;
  }

  export interface Value {}

  export interface FlowElement {
    id: string;
    xmlRowNumber: number;
    xmlColumnNumber: number;
    extensionElements: ExtensionElement;
    attributes: Attribute;
    name: string;
    documentation: string;
    executionListeners: ExecutionListener[];
    values: Value;
  }
}
