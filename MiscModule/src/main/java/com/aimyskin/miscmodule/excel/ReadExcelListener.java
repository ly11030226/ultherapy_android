package com.aimyskin.miscmodule.excel;

public interface ReadExcelListener {
    void readRowStart();
    void readCellsExcel(int rowIndex, int cellsIndex, String content);
    void readRowEnd();
}
