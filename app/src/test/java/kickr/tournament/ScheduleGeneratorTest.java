package kickr.tournament;

import kickr.service.tournament.ScheduleGenerator;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;

/**
 *
 * @author nikku
 */
public class ScheduleGeneratorTest {

  private static final List<String> PLAYERS = Arrays.asList("T", "M", "N", "S", "D", "K", "L", "U");

  @Test
  public void shouldGenerateOnce() throws Exception {
    System.out.println(ScheduleGenerator.createSchedule(PLAYERS, 30));
    System.out.println();
  }

  @Test
  public void shouldGenerateRepeatedly() throws Exception {
    ScheduleGenerator<String> generator = new ScheduleGenerator<>(PLAYERS);

    System.out.println(generator.generate(30));
    System.out.println();
    System.out.println(generator.generate(30));
    System.out.println();
    System.out.println(generator.generate(30));
    System.out.println();
    System.out.println(generator.generate(30));
    System.out.println();
    System.out.println(generator.generate(30));
    System.out.println();
  }

  @Test
  public void shouldRemovePlayersMonotonously() throws Exception {
    ScheduleGenerator<String> generator = new ScheduleGenerator<>(PLAYERS);

    generator.generate(30);
    System.out.println("\n---------\n");

    generator.removePlayer("T");
    System.out.println("\n---------\n");
    
    generator.removePlayer("M");
  }
}
