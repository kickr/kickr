package kickr.web.model;

import java.util.ArrayList;
import java.util.List;

import kickr.db.entity.FoosballTable;

public class FoosballTableData {
  
  protected Long id;
  protected String name;
  protected String team1Alias;
  protected String team2Alias;
  
  public static FoosballTableData fromTable(FoosballTable table) {
    FoosballTableData tableData = new FoosballTableData();
    
    tableData.id = table.getId();
    tableData.team1Alias = table.getTeam1Alias();
    tableData.team2Alias = table.getTeam2Alias();
    tableData.name = table.getName();
    
    return tableData;
  }
  
  public static List<FoosballTableData> fromTables(List<FoosballTable> tables) {
    List<FoosballTableData> tablesData = new ArrayList<>();
    
    for (FoosballTable table : tables) {
      tablesData.add(fromTable(table));
    }
    
    return tablesData;
  }

  public FoosballTableData() { }
  
  public FoosballTableData(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getTeam1Alias() {
    return team1Alias;
  }

  public void setTeam1Alias(String team1Alias) {
    this.team1Alias = team1Alias;
  }

  public String getTeam2Alias() {
    return team2Alias;
  }

  public void setTeam2Alias(String team2Alias) {
    this.team2Alias = team2Alias;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }
}
