package com.dtstack.dtcenter.loader.client.table;

import com.dtstack.dtcenter.loader.ClassLoaderCallBackMethod;
import com.dtstack.dtcenter.loader.client.ITable;
import com.dtstack.dtcenter.loader.dto.UpsertColumnMetaDTO;
import com.dtstack.dtcenter.loader.dto.source.ISourceDTO;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

/**
 * 表相关操作客户端代理类
 *
 * @author ：wangchuan
 * date：Created in 2:17 下午 2020/11/12
 * company: www.dtstack.com
 */
public class TableClientProxy implements ITable {

    ITable targetClient;

    public TableClientProxy(ITable table) {
        this.targetClient = table;
    }

    @Override
    public Connection getCon(ISourceDTO source) {
        return ClassLoaderCallBackMethod.callbackAndReset(() -> targetClient.getCon(source),
                targetClient.getClass().getClassLoader());
    }

    @Override
    public List<Map<String, Object>> executeQuery(ISourceDTO source, String sql) {
        return ClassLoaderCallBackMethod.callbackAndReset(() -> targetClient.executeQuery(source, sql),
                targetClient.getClass().getClassLoader());
    }

    @Override
    public Boolean executeSqlWithoutResultSet(ISourceDTO source, String sql) {
        return ClassLoaderCallBackMethod.callbackAndReset(() -> targetClient.executeSqlWithoutResultSet(source, sql),
                targetClient.getClass().getClassLoader());
    }

    @Override
    public List<String> showPartitions(ISourceDTO source, String tableName) {
        return ClassLoaderCallBackMethod.callbackAndReset(() -> targetClient.showPartitions(source, tableName),
                targetClient.getClass().getClassLoader());
    }

    @Override
    public Boolean dropTable(ISourceDTO source, String tableName) {
        return ClassLoaderCallBackMethod.callbackAndReset(() -> targetClient.dropTable(source, tableName),
                targetClient.getClass().getClassLoader());
    }

    @Override
    public Boolean renameTable(ISourceDTO source, String oldTableName, String newTableName) {
        return ClassLoaderCallBackMethod.callbackAndReset(() -> targetClient.renameTable(source, oldTableName, newTableName),
                targetClient.getClass().getClassLoader());
    }

    @Override
    public Boolean alterTableParams(ISourceDTO source, String tableName, Map<String, String> params) {
        return ClassLoaderCallBackMethod.callbackAndReset(() -> targetClient.alterTableParams(source, tableName, params),
                targetClient.getClass().getClassLoader());
    }

    @Override
    public Long getTableSize(ISourceDTO source, String schema, String tableName) {
        return ClassLoaderCallBackMethod.callbackAndReset(() -> targetClient.getTableSize(source, schema, tableName),
                targetClient.getClass().getClassLoader());
    }

    @Override
    public Boolean isView(ISourceDTO source, String schema, String tableName) {
        return ClassLoaderCallBackMethod.callbackAndReset(() -> targetClient.isView(source, schema, tableName),
                targetClient.getClass().getClassLoader());
    }

    @Override
    public Boolean upsertTableColumn(ISourceDTO source, UpsertColumnMetaDTO columnMetaDTO) {
        return ClassLoaderCallBackMethod.callbackAndReset(() -> targetClient.upsertTableColumn(source, columnMetaDTO),
                targetClient.getClass().getClassLoader());
    }
}
