package kickr.core.model;

import java.util.ArrayList;
import java.util.List;

import kickr.db.entity.FoosballTable;

public class TableData {
  
  protected String name;
  protected String team1Alias;
  protected String team2Alias;
  
  public static TableData fromTable(FoosballTable table) {
    TableData tableData = new TableData();
    
    tableData.team1Alias = table.getTeam1Alias();
    tableData.team2Alias = table.getTeam2Alias();
    tableData.name = table.getName();
    
    return tableData;
  }
  
  public static List<TableData> fromTables(List<FoosballTable> tables) {
    List<TableData> tablesData = new ArrayList<TableData>();
    
    for (FoosballTable table : tables) {
      tablesData.add(fromTable(table));
    }
    
    return tablesData;
  }
}
