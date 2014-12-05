package relationship

import org.apache.log4j.Logger

import attribute.Attribute
import relationship.Relationship
import utils.Status
import entity.Entity
import entity.Index

/**
 * Связи между таблицами (FK).
 */
//добавить имя?
public class Relationship implements Serializable {

	static Logger log = Logger.getLogger(Relationship.class.getName())
	
	private long id
	
	private List<Attribute> toAttr = new ArrayList<>()
	
	private long fromEntityId
	
	private long fromEntityTime
	
	private long toEntityId
	
	private long toEntityTime
	
	private long time
	
	private boolean isDeleted
	
	private Status status
	
	private long indexId
	
	private long indexTime
	
	public static class Cardinality implements Serializable {
		
		public enum CardinalityType {
			
			ZERO_ONE_OR_MORE (1),
			ONE_OR_MORE (2),
			ZERO_OR_ONE (3),
			EXACTLY (4);
			
			private long number
			
			private CardinalityType(long number) {
				this.number = number
			}
			
			public static List<String> getAllTypes() {
				List<String> types = new ArrayList<>()
				
				for (CardinalityType type : CardinalityType.values()) {
					types.add(type.number)
				}
				
				return types
			}
			
			public static CardinalityType getByNumber(long number) {
				for (CardinalityType type : CardinalityType.values()) {
					if(type.number == number) {
						return type
					}
				}
				
				return ZERO_ONE_OR_MORE
			}
		}
		
		private boolean identifying
		
		/**
		 * Степень отношения для случаев, когда она явным образом задана.
		 */
		private long cardinalityNumber
		
		private CardinalityType cardinalityType
	}
	
	private Cardinality cardinality = new Cardinality()
}
