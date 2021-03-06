/**************************************************************************************** 
 Copyright © 2003-2012 hbasesoft Corporation. All rights reserved. Reproduction or       <br>
 transmission in whole or in part, in any form or by any means, electronic, mechanical <br>
 or otherwise, is prohibited without the prior written consent of the copyright owner. <br>
 ****************************************************************************************/
package com.hbasesoft.framework.common.utils;

import java.util.Map;

import org.junit.Test;

import com.hbasesoft.framework.common.ErrorCodeDef;

/**
 * <Description> <br>
 * 
 * @author 王伟<br>
 * @version 1.0<br>
 * @taskId <br>
 * @CreateDate 2018年4月19日 <br>
 * @since V1.0<br>
 * @see com.hbasesoft.framework.common <br>
 */
public class PropertyHolderTest {

    @Test
    public void getProperties() {
        Map<String, String> map = PropertyHolder.getProperties();
        // 这个属性定义在 application.yml里面
        Assert.isTrue(map.containsKey("test.str.str1"), ErrorCodeDef.SYSTEM_ERROR_10001);

        // 这个定义在ext01.properties里面
        Assert.isTrue(map.containsKey("test01"), ErrorCodeDef.SYSTEM_ERROR_10001);

        // 这个定义在ext02.yml里面
        Assert.isTrue(map.containsKey("test02"), ErrorCodeDef.SYSTEM_ERROR_10001);
    }

    @Test
    public void getBooleanProperty() {
        boolean b1 = PropertyHolder.getBooleanProperty("test.bool.bool1");
        Assert.isTrue(b1, ErrorCodeDef.SYSTEM_ERROR_10001);

        boolean b2 = PropertyHolder.getBooleanProperty("test.bool.none", false);
        Assert.isFalse(b2, ErrorCodeDef.SYSTEM_ERROR_10001);
    }

    @Test
    public void getIntProperty() {
        int a = PropertyHolder.getIntProperty("test.int.int1");
        Assert.isTrue(a == 1, ErrorCodeDef.SYSTEM_ERROR_10001);

        a = PropertyHolder.getIntProperty("test.int.none", 3);
        Assert.isTrue(a == 3, ErrorCodeDef.SYSTEM_ERROR_10001);
    }

    @Test
    public void getLongProperty() {
        long a = PropertyHolder.getLongProperty("test.long.long1");
        Assert.isTrue(a == 1000l, ErrorCodeDef.SYSTEM_ERROR_10001);

        a = PropertyHolder.getLongProperty("test.long.none", 3l);
        Assert.isTrue(a == 3l, ErrorCodeDef.SYSTEM_ERROR_10001);
    }

    @Test
    public void getProperty() {
        String a = PropertyHolder.getProperty("test.str.str2");
        Assert.equals(a, "bcd", ErrorCodeDef.SYSTEM_ERROR_10001);

        a = PropertyHolder.getProperty("test.str.none", "abcdefg");
        Assert.equals(a, "abcdefg", ErrorCodeDef.SYSTEM_ERROR_10001);

        a = PropertyHolder.getProperty("test.int.int2");
        Assert.equals(a, "-1", ErrorCodeDef.SYSTEM_ERROR_10001);

        a = PropertyHolder.getProperty("test.long.long2");
        Assert.equals(a, "3000", ErrorCodeDef.SYSTEM_ERROR_10001);

        a = PropertyHolder.getProperty("test.bool.bool2");
        Assert.equals(a, "false", ErrorCodeDef.SYSTEM_ERROR_10001);
    }

    @Test
    public void setProperty() {
        String a = PropertyHolder.getProperty("test.none");
        Assert.isNull(a, ErrorCodeDef.SYSTEM_ERROR_10001);

        PropertyHolder.setProperty("test.none", "你好");
        a = PropertyHolder.getProperty("test.none");
        Assert.equals(a, "你好", ErrorCodeDef.SYSTEM_ERROR_10001);
    }

    @Test
    public void getProjectName() {
        String projectName = PropertyHolder.getProjectName();
        Assert.equals(projectName, "demo", ErrorCodeDef.SYSTEM_ERROR_10001);
    }

    @Test
    public void getErrorMessage() {

        // 错误描述取自 errorMessage.properties
        String e1 = PropertyHolder.getErrorMessage(ErrorCodeDef.SYSTEM_ERROR_10001);
        Assert.equals(e1, "系统错误", ErrorCodeDef.SYSTEM_ERROR_10001);

        // 没有配置错误文件
        String e2 = PropertyHolder.getErrorMessage(-1);
        Assert.equals(e2, "-1", ErrorCodeDef.SYSTEM_ERROR_10001);

        // 取项目名称所对应的错误文件 demo_errorMessage.properties
        String e3 = PropertyHolder.getErrorMessage(8888);
        Assert.equals(e3, "demo", ErrorCodeDef.SYSTEM_ERROR_10001);

        // 取扩展文件中对应的错误文件 ext_errorMessage.properties
        String e4 = PropertyHolder.getErrorMessage(9998,"a");
        Assert.equals(e4, "测试a测试", ErrorCodeDef.SYSTEM_ERROR_10001);
        
        String e5 = PropertyHolder.getErrorMessage(9999);
        Assert.equals(e5, "测试", ErrorCodeDef.SYSTEM_ERROR_10001);
    }

}
