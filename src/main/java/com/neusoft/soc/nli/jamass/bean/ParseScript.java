package com.neusoft.soc.nli.jamass.bean;

import com.neusoft.soc.nli.jamass.channel.parse.ParseFunction;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.io.Serializable;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by kokoharry on 2017/5/12.
 */
public class ParseScript implements Serializable{
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 4835664371906372019L;
    /**
     * ��������
     */
    private ParseFunction parseFunction;
    /**
     * ������Ӧ�豸����
     */
    private int dev_type;
    /**
     * �������� ��Ӧ�����ֶλ�ȡ ���� ˳���ȡ
     */
    private int parse_type;
    /**
     * ��������id
     */
    private String id;
    /**
     * ����������ؼ�����ӳ��
     */
    private Map<String,Pattern> mustPatternMap;
    /**
     * ����������ǹؼ�����ӳ��
     */
    private Map<String,Pattern> patternMap;
    /**
     * CollectorEvent���Ժ�������ʽӳ���ϵ �ؼ�����
     */
    private Map<String,String> attribute_regular_mapping_must;
    /**
     * CollectorEvent���Ժ�������ʽӳ���ϵ �ǹؼ�����
     */
    private Map<String,String> attribute_regular_mapping;
    /**
     * CollectorEvent��������Ҫ����ת���ֶ� ���磺10001 --�� �߼�
     */
    private Map<String,Map<String,String>> attribute_key_value;
    /**
     * ˳������Ķ�Ӧ����˳���,��������
     */
    private Map<String,Integer> attribute_sque;
    /**
     * ��ЩΪ�ؼ�����
     */
    private Map<Integer,String> attribute_sque_must_value;
    /**
     * ˳������õ��ָ���
     */
    private String split_sign;
    /**
     * �ɹ���������
     */
    private long parse_number;

    /**
     * ����������ʹ��
     */
    public void add_parse_number(){
        parse_number++;
    }

    public long getParse_number() {
        return parse_number;
    }

    public void setParse_number(long parse_number) {
        this.parse_number = parse_number;
    }

    public String getSplit_sign() {
        return split_sign;
    }

    public void setSplit_sign(String split_sign) {
        this.split_sign = split_sign;
    }



    public int getDev_type() {
        return dev_type;
    }

    public void setDev_type(int dev_type) {
        this.dev_type = dev_type;
    }

    public int getParse_type() {
        return parse_type;
    }

    public void setParse_type(int parse_type) {
        this.parse_type = parse_type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Map<String, String> getAttribute_regular_mapping_must() {
        return attribute_regular_mapping_must;
    }

    public void setAttribute_regular_mapping_must(Map<String, String> attribute_regular_mapping_must) {
        this.attribute_regular_mapping_must = attribute_regular_mapping_must;
    }

    public Map<String, String> getAttribute_regular_mapping() {
        return attribute_regular_mapping;
    }

    public void setAttribute_regular_mapping(Map<String, String> attribute_regular_mapping) {
        this.attribute_regular_mapping = attribute_regular_mapping;
    }

    public Map<String, Map<String, String>> getAttribute_key_value() {
        return attribute_key_value;
    }

    public void setAttribute_key_value(Map<String, Map<String, String>> attribute_key_value) {
        this.attribute_key_value = attribute_key_value;
    }

    public Map<String, Integer> getAttribute_sque() {
        return attribute_sque;
    }

    public void setAttribute_sque(Map<String, Integer> attribute_sque) {
        this.attribute_sque = attribute_sque;
    }

    public Map<String, Pattern> getMustPatternMap() {
        return mustPatternMap;
    }

    public void setMustPatternMap(Map<String, Pattern> mustPatternMap) {
        this.mustPatternMap = mustPatternMap;
    }

    public Map<String, Pattern> getPatternMap() {
        return patternMap;
    }

    public void setPatternMap(Map<String, Pattern> patternMap) {
        this.patternMap = patternMap;
    }

    public ParseFunction getParseFunction() {
        return parseFunction;
    }

    public void setParseFunction(ParseFunction parseFunction) {
        this.parseFunction = parseFunction;
        this.parseFunction.setParseScript(this);
    }

    public Map<Integer, String> getAttribute_sque_must_value() {
        return attribute_sque_must_value;
    }

    public void setAttribute_sque_must_value(Map<Integer, String> attribute_sque_must_value) {
        this.attribute_sque_must_value = attribute_sque_must_value;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }

}
