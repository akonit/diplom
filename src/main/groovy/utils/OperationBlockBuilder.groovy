package utils

public final class OperationBlockBuilder {

	private OperationBlock operationBlock
	
	public OperationBlockBuilder() {
		operationBlock = new OperationBlock();
	}
	
	public OperationBlockBuilder redo(Operation op) {
		operationBlock.redo.add(op)
		return this
	}
	
	public OperationBlockBuilder undo(Operation op) {
		operationBlock.undo.add(op)
		return this
	}
	
	public OperationBlock build() {
		return operationBlock
	}
}
