package kafkaqueue;

import java.nio.ByteBuffer;
import java.util.Map;

import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Serializer;

import student.proto.StudentOuterClass.Student;

public class SerializerStudent implements Serializer<Student> {
	private String encoding = "UTF8";

	public void close() {

	}

	public void configure(Map<String, ?> arg0, boolean data) {
	}

	public byte[] serialize(String arg0, Student data) {

		int sizeOfName;
		int sizeOfId;
		int sizeOfSchool;
		byte[] serializedName;
		byte[] serializedId;
		byte[] serializedSchool;

		try {
			if (data == null)
				return null;
	
			serializedName = data.getName().getBytes(encoding);
			sizeOfName = serializedName.length;
			
			
			serializedId = Integer.toString(data.getId()).getBytes(encoding);
			sizeOfId = serializedId.length;
			
			serializedSchool = data.getSchool().getBytes(encoding);
			sizeOfSchool = serializedSchool.length;

			ByteBuffer buf = ByteBuffer.allocate(4+sizeOfName+4+sizeOfId+4+sizeOfSchool);
			
			buf.putInt(sizeOfName);
			buf.put(serializedName);
			
			buf.putInt(sizeOfId);
			buf.put(serializedId);
			
			buf.putInt(sizeOfSchool);
			buf.put(serializedSchool);
			
			return buf.array();
			
			

		} catch (Exception e) {
			System.out.println(e.getStackTrace());
			throw new SerializationException("Error when serializing");
			
		}

		
	}


}