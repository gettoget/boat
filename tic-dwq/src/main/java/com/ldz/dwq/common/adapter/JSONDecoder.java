package com.ldz.dwq.common.adapter;


import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ldz.dwq.common.bean.JsonBean;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.SocketException;
import java.nio.charset.Charset;
import java.util.List;

/**
 * 数据解码器，将接收到的字节数据，按照协议格式封装成bean结构。接收数据格式为JSON字符串
 * 一次只接收到一个指令数据
 * {
	"taskId": "",//任务编号
	"accId": "",//事件编号
	"accReportId": "",//事件下某个接报信息
	"subSysType": "",//子系统类型（SGZT分析子系统、YX子系统、FSC子系统…）
	"subSysCode": "",//子系统编码
	"subSysStatus": "",//针对某次接报子系统计算状态（未读、已读、正在分析、已上报、已暂停、已停止、异常…）
	"briefResults": "",//子系统针对某次接报计算结果文件的ftp存储目录
	"dataFileNames": "",//用户操作分系统在向计算子系统发送任务时携带的所需其他计算子系统提供的计算结果文件的ftp存储目录
	"analysisSchedule": "",//针对某次接报计算进度（百分比）
	"templateName": "",//镜像模板名称
	"hostName": "",//主机名称
	"accBaseName": "",//事发基地名称
	"lng": "",//事发地点经度
	"lat": "",//事发地点纬度
	"happenTime": ""//事发时刻
   }
   
     服务器发送数据格式
   S2C
   startAnalysis| {"accId":1120,"accReportId":1140,"analysisiType":300101,"subSysCode":"D04","subSysType":301304,"taskContent":"2019-04-15测试 by sgc","taskId":1441,"taskTitle":"ceshi","userId":1,"dataFileNames":[{"subSysCode":301302,"filename":"yx/2g1z492e328371d94a8376f327d03e6/fast/results.rar"},{"subSysCode":301303,"filename":"fsc/2g1z492e328371d94a8376f327d03e6/fast/results.rar"}],"accBaseName":"qianyi","lng":"120.11","lat":"40.23","happenTime":"2019-01-01 10:10:10"}
     客户端响应数据格式
   C2S
   startAnalysis|{"taskId":1441,"userId":0,"accId":1120,"accReportId":1140,"analysisiType":300101,"subSysType":301304,"subSysCode":"D04","subSysStatus":"已读"}
 *
 */
public class JSONDecoder extends MessageToMessageDecoder<ByteBuf> {

    Logger socketLog = LoggerFactory.getLogger("access_info");
    ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    
//    protected NettyConfig mNettyConfig;
    
    public JSONDecoder(){
//    	this.mNettyConfig = nettyConfig;
    }
    
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
        try{
        	String hexData = ByteBufUtil.hexDump(msg);


//			byte[] pack = ByteBufUtil.decodeHexDump(hexData.substring(0,8));
//			String Package = new String(pack, Charset.forName("UTF-8"));
//			byte[] head = ByteBufUtil.decodeHexDump(hexData.substring(8,12));
//			String Header = new String(head,Charset.forName("UTF-8"));
//			byte[] pro = ByteBufUtil.decodeHexDump(hexData.substring(12,16));
//			String Protocol = new String(pro, Charset.forName("UTF-8"));
//			byte[] ope = ByteBufUtil.decodeHexDump(hexData.substring(16,24));
//			String Operation = new String(ope, Charset.forName("UTF-8"));
//			byte[] seq = ByteBufUtil.decodeHexDump(hexData.substring(24,32));
//			String Seq = new String(ope, Charset.forName("UTF-8"));
			byte[] jsonBytes =ByteBufUtil.decodeHexDump(hexData.substring(32));
			String jsonSll = new String(jsonBytes, Charset.forName("UTF-8"));
//			JsonBean  j = new JsonBean();
////			j.setPackage(Package);
////			j.setBody(jsonSll);
////			j.setHeader(Header);
////			j.setOperation(Operation);
////			j.setProtocol(Protocol);
////			j.setSeq(Seq);
			socketLog.info("接收数据:"+hexData);
//			System.out.println(JSON.toJSONString(j));

        }catch(Exception e){
        	socketLog.error("Socket数据解析异常", e);
        	throw new SocketException("数据解析异常");
        }    
    }
}
