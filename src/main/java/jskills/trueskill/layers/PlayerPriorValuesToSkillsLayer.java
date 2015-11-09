package jskills.trueskill.layers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import jskills.Rating;
import jskills.util.GaussianDistribution;
import jskills.util.MathUtils;
import jskills.trueskill.TrueSkillFactorGraph;
import jskills.trueskill.factors.GaussianPriorFactor;
import jskills.Player;
import jskills.Team;
import jskills.factorgraphs.DefaultVariable;
import jskills.factorgraphs.KeyedVariable;
import jskills.factorgraphs.Schedule;
import jskills.factorgraphs.ScheduleStep;
import jskills.factorgraphs.Variable;


// We intentionally have no Posterior schedule since the only purpose here is to 
public class PlayerPriorValuesToSkillsLayer extends
    TrueSkillFactorGraphLayer<DefaultVariable<GaussianDistribution>, 
                              GaussianPriorFactor,
                              KeyedVariable<Player, GaussianDistribution>> {

    private final Collection<Team> teams;

    public PlayerPriorValuesToSkillsLayer(TrueSkillFactorGraph parentGraph, Collection<Team> teams) {
        super(parentGraph);
        this.teams = teams;
    }

    @Override
    public void buildLayer() {
        for(Team currentTeam : teams) {
            List<KeyedVariable<Player, GaussianDistribution>> currentTeamSkills = new ArrayList<>();

            for (Player currentTeamPlayer : currentTeam.getPlayers()) {
                KeyedVariable<Player, GaussianDistribution> playerSkill =
                    createSkillOutputVariable(currentTeamPlayer);
                AddLayerFactor(createPriorFactor(currentTeamPlayer, currentTeam.getRating(currentTeamPlayer), playerSkill));
                currentTeamSkills.add(playerSkill);
            }

            addOutputVariableGroup(currentTeamSkills);
        }
    }

    @Override
    public Schedule<GaussianDistribution> createPriorSchedule() {
        Collection<Schedule<GaussianDistribution>> schedules = new ArrayList<>();
        for (GaussianPriorFactor prior : getLocalFactors()) {
            schedules.add(new ScheduleStep<>("Prior to Skill Step", prior, 0));
        }
        return ScheduleSequence(schedules, "All priors");
    }

    private GaussianPriorFactor createPriorFactor(Player player, Rating priorRating,
                                                  Variable<GaussianDistribution> skillsVariable) {
        return new GaussianPriorFactor(priorRating.getMean(),
                                       MathUtils.square(priorRating.getStandardDeviation()) +
                                       MathUtils.square(getParentFactorGraph().getGameInfo().getDynamicsFactor()), skillsVariable);
    }

    private KeyedVariable<Player, GaussianDistribution> createSkillOutputVariable(Player key) {
        return new KeyedVariable<>(key, GaussianDistribution.UNIFORM, "%s's skill", key.toString());
    }
}