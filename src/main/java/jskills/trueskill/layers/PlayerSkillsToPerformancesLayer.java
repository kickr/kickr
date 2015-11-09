package jskills.trueskill.layers;

import jskills.factorgraphs.KeyedVariable;
import jskills.factorgraphs.Schedule;
import jskills.factorgraphs.ScheduleStep;
import jskills.util.GaussianDistribution;
import jskills.util.MathUtils;
import jskills.trueskill.TrueSkillFactorGraph;
import jskills.trueskill.factors.GaussianLikelihoodFactor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import jskills.Player;

public class PlayerSkillsToPerformancesLayer extends
    TrueSkillFactorGraphLayer<KeyedVariable<Player, GaussianDistribution>,
                              GaussianLikelihoodFactor,
                              KeyedVariable<Player, GaussianDistribution>> {

    public PlayerSkillsToPerformancesLayer(TrueSkillFactorGraph parentGraph)
    {
        super(parentGraph);
    }

    @Override
    public void buildLayer() {
        for(List<KeyedVariable<Player, GaussianDistribution>> currentTeam : getInputVariablesGroups()) {
            List<KeyedVariable<Player, GaussianDistribution>> currentTeamPlayerPerformances = new ArrayList<>();

            for(KeyedVariable<Player, GaussianDistribution> playerSkillVariable : currentTeam) {
                KeyedVariable<Player, GaussianDistribution> playerPerformance =
                    createOutputVariable(playerSkillVariable.getKey());
                AddLayerFactor(createLikelihood(playerSkillVariable, playerPerformance));
                currentTeamPlayerPerformances.add(playerPerformance);
            }

            addOutputVariableGroup(currentTeamPlayerPerformances);
        }
    }

    private GaussianLikelihoodFactor createLikelihood(KeyedVariable<Player, GaussianDistribution> playerSkill,
                                                      KeyedVariable<Player, GaussianDistribution> playerPerformance) {
        return new GaussianLikelihoodFactor(MathUtils.square(parentFactorGraph.getGameInfo().getBeta()), playerPerformance, playerSkill);
    }

    private KeyedVariable<Player, GaussianDistribution> createOutputVariable(Player key) {
        return new KeyedVariable<>(key, GaussianDistribution.UNIFORM, "%s's performance", key);
    }

    @Override
    public Schedule<GaussianDistribution> createPriorSchedule() {
        Collection<Schedule<GaussianDistribution>> schedules = new ArrayList<>();
        for (GaussianLikelihoodFactor likelihood : getLocalFactors()) {
            schedules.add(new ScheduleStep<>("Skill to Perf step", likelihood, 0));
        }
        return ScheduleSequence(schedules, "All skill to performance sending");
    }

    @Override
    public Schedule<GaussianDistribution> createPosteriorSchedule() {
        Collection<Schedule<GaussianDistribution>> schedules = new ArrayList<>();
        for (GaussianLikelihoodFactor likelihood : getLocalFactors()) {
            schedules.add(new ScheduleStep<>("Skill to Perf step", likelihood, 1));
        }
        return ScheduleSequence(schedules, "All skill to performance sending");
    }
}