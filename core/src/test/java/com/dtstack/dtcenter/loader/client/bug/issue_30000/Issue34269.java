package com.dtstack.dtcenter.loader.client.bug.issue_30000;

import com.dtstack.dtcenter.loader.IDownloader;
import com.dtstack.dtcenter.loader.client.ClientCache;
import com.dtstack.dtcenter.loader.client.IHdfsFile;
import com.dtstack.dtcenter.loader.client.IKerberos;
import com.dtstack.dtcenter.loader.client.sql.HdfsKerberosTest;
import com.dtstack.dtcenter.loader.dto.SqlQueryDTO;
import com.dtstack.dtcenter.loader.dto.source.HdfsSourceDTO;
import com.dtstack.dtcenter.loader.kerberos.HadoopConfTool;
import com.dtstack.dtcenter.loader.source.DataSourceType;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * 适配hadoop 3.0.0之后 yarn index log日志下载
 *
 * @author ：wangchuan
 * date：Created in 1:56 下午 2021/1/26
 * company: www.dtstack.com
 */
@Ignore
public class Issue34269 {

    private static HdfsSourceDTO source = HdfsSourceDTO.builder()
            .defaultFS("hdfs://172.16.101.23:8020")
            .build();

    @BeforeClass
    public static void beforeClass() {
        // 准备 Kerberos 参数
        Map<String, Object> kerberosConfig = new HashMap<>();
        kerberosConfig.put(HadoopConfTool.PRINCIPAL, "hive/ladp001.dtstack.com@DTSTACK.COM");
        kerberosConfig.put(HadoopConfTool.PRINCIPAL_FILE, "/hive.keytab");
        kerberosConfig.put(HadoopConfTool.KEY_JAVA_SECURITY_KRB5_CONF, "/krb5.conf");
        kerberosConfig.put("dfs.namenode.kerberos.principal", "hdfs/_HOST@DTSTACK.COM");
        source.setKerberosConfig(kerberosConfig);
        String localKerberosPath = HdfsKerberosTest.class.getResource("/cdp").getPath();
        IKerberos kerberos = ClientCache.getKerberos(DataSourceType.HDFS.getVal());
        kerberos.prepareKerberosForConnect(kerberosConfig, localKerberosPath);
        HashMap<String, Object> yarnConf = Maps.newHashMap();
        yarnConf.put("yarn.log-aggregation.file-formats", "IFile,TFile");
        yarnConf.put("dfs.namenode.kerberos.principal", "hdfs/_HOST@DTSTACK.COM");
        source.setYarnConf(yarnConf);
        source.setReadLimit(1024 * 1024 * 30);
        source.setAppIdStr("application_1610953567506_0002");
    }

    @Test
    public void testYarnLogDownload () {
        IHdfsFile client = ClientCache.getHdfs(DataSourceType.HDFS.getVal());
        IDownloader logDownloader = client.getLogDownloader(source, SqlQueryDTO.builder().build());
        while (!logDownloader.reachedEnd()) {
            Assert.assertTrue(StringUtils.isNotBlank(logDownloader.readNext().toString()));
        }
    }
}
