package server;
import java.util.Scanner;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message.Builder;
import com.google.protobuf.util.JsonFormat;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.util.ReferenceCountUtil;
import kafkaqueue.Producers;
import student.proto.StudentOuterClass.Student;

// Handles the server

public class Handler extends ChannelInboundHandlerAdapter { 

	static String topic = null;

	@SuppressWarnings("static-access")
	@Override
	public void channelRead(ChannelHandlerContext context, Object ob) throws InvalidProtocolBufferException {
		String request = null ;
		String json = null;
		FullHttpRequest HttpRequest = null;
//conversion of json to protobuff
		try
		{

			HttpRequest = (FullHttpRequest) ob;
			request = 	HttpRequest.method().toString() ;
			System.out.println("Request is " + request);
			Builder obj = Student.newBuilder();

			Student Student = null;
			if(request.equals("GET") || request.equals("POST"))
			{


				if(request.equals("POST"))
				{

					byte[] x = new byte[HttpRequest.content().capacity()];

					HttpRequest.content().readBytes(x);

					json = new String(x);

					JsonFormat.parser().merge(json, obj );

					Student = (Student) obj.build();

					System.out.println("Json converted to protobuf");

				}

				else
				{
					@SuppressWarnings("deprecation")
					QueryStringDecoder decoder = new QueryStringDecoder(HttpRequest.getUri());

					System.out.println("name is "+decoder.parameters().get("name").get(0));

					Student = Student.newBuilder()
							.setName(decoder.parameters().get("name").get(0))
							.setId(Integer.parseInt(decoder.parameters().get("id").get(0)))
							.setSchool(decoder.parameters().get("school").get(0))
							.build();

				}


				System.out.println(Student.toString());


				if(topic==null)
				{	Scanner s = new Scanner(System.in);
				System.out.print("Enter Topic Name :");
				topic = s.next();
				s.close();
				}
				Producers.producer(topic,Student);  
			}
		} finally {
			ReferenceCountUtil.release(ob); 
		}

	}

	@Override
	public void exceptionCaught(ChannelHandlerContext context, Throwable th) { 
		th.printStackTrace();
		context.close();
	}
}
