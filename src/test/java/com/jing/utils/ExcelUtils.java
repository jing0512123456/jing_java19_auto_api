package com.jing.utils;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.jing.constant.Constant;
import com.jing.pojo.CaseInfo;
import com.jing.pojo.WriteBackData;
import org.apache.poi.ss.usermodel.*;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExcelUtils {
    //批量回写集合
    public static List<WriteBackData> list = new ArrayList<>();

    /**
     * @param sheetIndex
     * @param sheetNum
     * @param cla
     * @return
     * @throws Exception
     */
    public static Object[] getdatas(int sheetIndex, int sheetNum, Class cla){
        List<CaseInfo> list = null;
        try {
            list = ExcelUtils.read(sheetIndex, sheetNum, cla);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Object[] datas = list.toArray();
        return datas;
    }


    /**
     * 读取excel并返回映射关系
     * @param sheetIndex   sheet开始位置
     * @param sheetNum      sheet个数
     * @param cla           映射关系字节码
     * @return
     * @throws Exception
     */
    public static List read(int sheetIndex,int sheetNum,Class cla) throws Exception {
        //1、加载excel文件
        //2、创建excel对象
        //3、读取sheet，row，cell
        //获取excel路径
        FileInputStream fis = new FileInputStream(Constant.Excel_PATH);
        //导入参数
        ImportParams params = new ImportParams();
        //从第？个sheet开始读取
        params.setStartSheetIndex(sheetIndex);
        //每次读取？个
        params.setSheetNum(sheetNum);
        List list = ExcelImportUtil.importExcel(fis, cla, params);
        return list;
    }

    /**
     * 批量回写
     */
    public static void batchWrite(){
        FileInputStream fis =null;
        FileOutputStream fos = null;
        try {
          fis = new FileInputStream(Constant.Excel_PATH);
          //循环 批量回写集合list
        Workbook excle = WorkbookFactory.create(fis);
            for (WriteBackData backData : list) {
                //获取rowNum
                int rowNum = backData.getRowNum();
                // //获取cellNum
                int cellNum = backData.getCellNum();
                //获取content
                String content = backData.getContent();
                //获取rsheetIndex
                int sheetIndex = backData.getSheetIndex();
                Sheet sheet = excle.getSheetAt(sheetIndex);
                Row row = sheet.getRow(rowNum);
                Cell cell = row.getCell(cellNum, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                cell.setCellValue(content);
            }
         fos = new FileOutputStream(Constant.Excel_PATH);
        excle.write(fos);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            //一定会执行（无论是否发生异常，都会执行）
            //释放资源
            try {
                if(fis!=null){
                    fis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (fos!=null){
                    fos.close() ;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}