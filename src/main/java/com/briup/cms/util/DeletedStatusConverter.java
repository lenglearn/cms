package com.briup.cms.util;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import org.springframework.stereotype.Component;

@Component
public class DeletedStatusConverter implements Converter<Integer> {

    @Override
    public WriteCellData<?> convertToExcelData(Integer deletedStatus, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        if (deletedStatus == null) {
            return new WriteCellData<>("");
        }
        return new WriteCellData<>(deletedStatus == 1 ? "被删除" : "未被删除");
    }

    @Override
    public Integer convertToJavaData(ReadCellData<?> cellData, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        // Implement if necessary
        return null;
    }
}
