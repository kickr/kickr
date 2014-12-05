package kickr.core.model;

import kickr.db.entity.FoosballTable;

public class TableData {

  protected String team1;
  protected String team2;
  
  public static TableData fromTable(FoosballTable table) {
    TableData tableData = new TableData();
    
    tableData.team1 = table.getTeam1Alias();
    tableData.team2 = table.getTeam2Alias();
    
    return tableData;
  }
}
