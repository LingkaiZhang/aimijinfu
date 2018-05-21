package com.yuanin.aimifinance.utils;

import com.yuanin.aimifinance.entity.WheelTwoModel;
import com.yuanin.aimifinance.entity.WheelThreeModel;
import com.yuanin.aimifinance.entity.WheelOneModel;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;


public class XmlParserHandler extends DefaultHandler {

    /**
     * 存储所有的解析对象
     */
    private List<WheelOneModel> provinceList = new ArrayList<WheelOneModel>();

    public XmlParserHandler() {

    }

    public List<WheelOneModel> getDataList() {
        return provinceList;
    }

    @Override
    public void startDocument() throws SAXException {
        // 当读到第一个开始标签的时候，会触发这个方法
    }

    WheelOneModel wheelOneModel = new WheelOneModel();
    WheelTwoModel wheelTwoModel = new WheelTwoModel();
    WheelThreeModel wheelThreeModel = new WheelThreeModel();

    @Override
    public void startElement(String uri, String localName, String qName,
                             Attributes attributes) throws SAXException {
        // 当遇到开始标记的时候，调用这个方法
        if (qName.equals("one")) {
            wheelOneModel = new WheelOneModel();
            wheelOneModel.setName(attributes.getValue(0));
            wheelOneModel.setWheelTwoList(new ArrayList<WheelTwoModel>());
        } else if (qName.equals("two")) {
            wheelTwoModel = new WheelTwoModel();
            wheelTwoModel.setName(attributes.getValue(0));
            wheelTwoModel.setWheelThreeList(new ArrayList<WheelThreeModel>());
        } else if (qName.equals("three")) {
            wheelThreeModel = new WheelThreeModel();
            wheelThreeModel.setName(attributes.getValue(0));
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName)
            throws SAXException {
        // 遇到结束标记的时候，会调用这个方法
        if (qName.equals("three")) {
            wheelTwoModel.getWheelThreeList().add(wheelThreeModel);
        } else if (qName.equals("two")) {
            wheelOneModel.getWheelTwoList().add(wheelTwoModel);
        } else if (qName.equals("one")) {
            provinceList.add(wheelOneModel);
        }
    }

    @Override
    public void characters(char[] ch, int start, int length)
            throws SAXException {
    }

}
