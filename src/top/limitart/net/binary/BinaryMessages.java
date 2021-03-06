/*
 * Copyright (c) 2016-present The Limitart Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package top.limitart.net.binary;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.limitart.net.binary.BinaryMessageProtoFileInfo.BinaryMessageFieldProtoInfo;
import top.limitart.net.binary.BinaryMessageProtoFileInfo.BinaryMessageMetaProtoInfo;
import top.limitart.net.binary.BinaryMessageProtoFileInfo.BinaryMessageProtoInfo;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Objects;
import java.util.stream.IntStream;

/**
 * 消息相关方法
 *
 * @author hank
 */
public final class BinaryMessages {
    private static final Logger LOGGER = LoggerFactory.getLogger(BinaryMessage.class);
    private static Validator xmlValidator;
    private static Configuration cfg;
    private static Template template;

    private BinaryMessages() {
    }

    public static String ID2String(short ID) {
        return String.format("0X%04x", ID).toUpperCase();
    }

    /**
     * 构造消息ID
     *
     * @param modID
     * @param mID
     * @return
     * @throws BinaryMessageIDException
     */
    public static short createID(int modID, int mID) {
        if (modID > Short.MAX_VALUE || modID < Short.MIN_VALUE) {
            LOGGER.error("modID error", new BinaryMessageIDException(modID));
        }
        if (mID > Short.MAX_VALUE || mID < Short.MIN_VALUE) {
            LOGGER.error("mID error", new BinaryMessageIDException(modID));
        }
        return (short) (modID << 8 | mID);
    }

    /**
     * 获取模块ID
     *
     * @param messageID
     * @return
     */
    public static byte modID(short messageID) {
        return (byte) (messageID >> 8);
    }

    /**
     * 获取内容ID
     *
     * @param messageID
     * @return
     */
    public static byte contentID(short messageID) {
        return (byte) (messageID & 0X00FF);
    }

    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException, URISyntaxException, TemplateException {
        BinaryMessageProtoFileInfo binaryMessageProtoFileInfo = readBinaryMessageProtoFile(new File(Class.class.getResource("/binary_msg_proto.xml").getPath()));
        generateMessageJava(binaryMessageProtoFileInfo, "d://test");
    }

    /**
     * 解析消息协议xml文件
     *
     * @param file
     * @return
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     */
    public static BinaryMessageProtoFileInfo readBinaryMessageProtoFile(File file)
            throws ParserConfigurationException, SAXException, IOException {
//        validateProtoFile(file);
        BinaryMessageProtoFileInfo info = new BinaryMessageProtoFileInfo();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(file);
        NodeList messagesList = document.getElementsByTagName("messages");
        Node messagesNode = messagesList.item(0);
        // 消息包信息
        String packageIDStr = messagesNode.getAttributes().getNamedItem("id").getNodeValue();
        String packageNameStr = messagesNode.getAttributes().getNamedItem("name").getNodeValue();
        String packageDeclareStr = messagesNode.getAttributes().getNamedItem("package").getNodeValue();
        String packageExplainStr = messagesNode.getAttributes().getNamedItem("explain").getNodeValue();
        info.set_id(Byte.parseByte(packageIDStr));
        info.set_name(packageNameStr);
        info.set_package(packageDeclareStr);
        info.set_explain(packageExplainStr);
        // meta解析
        NodeList metaList = messagesNode.getChildNodes();
        for (int i = 0; i < metaList.getLength(); ++i) {
            Node messageItem = metaList.item(i);
            if (messageItem == null) {
                continue;
            }
            if (messageItem.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }
            if (messageItem.getNodeName() == "meta") {
                BinaryMessageMetaProtoInfo metaInfo = new BinaryMessageMetaProtoInfo();
                String metaNameStr = messageItem.getAttributes().getNamedItem("name").getNodeValue();
                String metaExplainStr = messageItem.getAttributes().getNamedItem("explain").getNodeValue();
                metaInfo.set_name(metaNameStr);
                metaInfo.set_explain(metaExplainStr);
                NodeList metaFieldList = messageItem.getChildNodes();
                IntStream.range(0, metaFieldList.getLength()).mapToObj(metaFieldList::item).filter(Objects::nonNull).filter(metaFieldItem -> metaFieldItem.getNodeType() == Node.ELEMENT_NODE).forEach(metaFieldItem -> metaInfo.get_fields().add(parseField(metaFieldItem)));
                info.get_metas().add(metaInfo);
            } else if (messageItem.getNodeName() == "message") {
                BinaryMessageProtoInfo messageInfo = new BinaryMessageProtoInfo();
                String messageNameStr = messageItem.getAttributes().getNamedItem("name").getNodeValue();
                String messageExplainStr = messageItem.getAttributes().getNamedItem("explain").getNodeValue();
                messageInfo.set_name(messageNameStr);
                messageInfo.set_explain(messageExplainStr);
                messageInfo.set_messageID((byte) i);
                NodeList messageFieldList = messageItem.getChildNodes();
                IntStream.range(0, messageFieldList.getLength()).mapToObj(messageFieldList::item).filter(Objects::nonNull).filter(messageFieldItem -> messageFieldItem.getNodeType() == Node.ELEMENT_NODE).forEach(messageFieldItem -> messageInfo.get_fields().add(parseField(messageFieldItem)));
                info.get_messages().add(messageInfo);
            }
        }
        return info;
    }

    private static BinaryMessageFieldProtoInfo parseField(Node metaFieldItem) {
        BinaryMessageFieldProtoInfo fieldInfo = new BinaryMessageFieldProtoInfo();
        String fieldTypeStr = metaFieldItem.getAttributes().getNamedItem("type").getNodeValue();
        String fieldNameStr = metaFieldItem.getAttributes().getNamedItem("name").getNodeValue();
        String fieldExplainStr = metaFieldItem.getAttributes().getNamedItem("explain").getNodeValue();
        fieldInfo.set_type(fieldTypeStr);
        fieldInfo.set_name(fieldNameStr);
        fieldInfo.set_explain(fieldExplainStr);
        String nodeName = metaFieldItem.getNodeName();
        if (nodeName.equals("list")) {
            fieldInfo.set_isList(1);
            fieldInfo.turnTypeToJavaBox();
        }
        return fieldInfo;
    }

    private static void validateProtoFile(File file) throws SAXException, IOException {
        if (xmlValidator == null) {
            xmlValidator = SchemaFactory
                    .newInstance("http://www.w3.org/2001/XMLSchema").newSchema(Class.class.getResource("/binary_msg_proto.xsd")).newValidator();
        }
        Source source = new StreamSource(file);
        xmlValidator.validate(source);
    }

    public static void generateMessageJava(BinaryMessageProtoFileInfo info, String outPath) throws URISyntaxException, IOException, ParserConfigurationException, SAXException, TemplateException {
        if (cfg == null) {
            cfg = new Configuration();
            cfg.setDirectoryForTemplateLoading(new File(Class.class.getResource("/").toURI()));
            template = cfg.getTemplate("message.ftl");
        }
        File outDir = new File(outPath + File.separator + info.get_package().replace('.', File.separatorChar));
        if (!outDir.exists()) {
            outDir.mkdirs();
        } else if (!outDir.isDirectory()) {
            throw new IOException("need a directory!");
        }
        try (FileWriter writer = new FileWriter(new File(outDir.getPath() + File.separator + info.get_name() + ".java"))) {
            template.process(info, writer);
        }
    }
}
