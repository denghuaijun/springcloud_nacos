package com.taikang.business.plugin;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.TopLevelClass;

import java.io.Serializable;
import java.util.List;

public class Plugin extends PluginAdapter {

    public boolean validate(List<String> list) {
        return true;
    }

    @Override
    public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass,
                                                 IntrospectedTable introspectedTable) {
        // TODO Auto-generated method stub
        FullyQualifiedJavaType fullyQualifiedJavaType = new FullyQualifiedJavaType(Serializable.class.getName());
        topLevelClass.addImportedType(Serializable.class.getName());
        topLevelClass.addSuperInterface(fullyQualifiedJavaType);
        Field field = new Field();
        field.setStatic(true);
        field.setName("serialVersionUID");
        field.setFinal(true);
        field.setType(new FullyQualifiedJavaType(long.class.getName()));
        field.setVisibility(JavaVisibility.PRIVATE);
        field.setInitializationString("1L");
        topLevelClass.addField(field);
//	    	private static final long serialVersionUID = 1L;
        return super.modelBaseRecordClassGenerated(topLevelClass, introspectedTable);
    }
}
