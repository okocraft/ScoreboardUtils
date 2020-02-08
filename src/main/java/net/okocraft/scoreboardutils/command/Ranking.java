package net.okocraft.scoreboardutils.command;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.util.StringUtil;

class Ranking extends BaseSubCommand {

	Ranking() {
		super(
			"ranking",
			3,
			"/scoreboardutils ranking <objective> [page]"
		);
	}

	@Override
	public boolean execute(CommandSender sender, String[] args) {
		Scoreboard mainScoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
		Objective objective = mainScoreboard.getObjective(args[1]);

		if (objective == null) {
			messages.objectiveDoesNotFound(sender);
			return false;
		}

		int page = 1;
		try {
			page = Integer.parseInt(args[2]);
			if (page < 1) {
				throw new NumberFormatException("The page must be more than 1");
			}
		} catch (NumberFormatException ignore) {
		}

		List<Score> entries = mainScoreboard.getEntries().stream().parallel()
				.map(objective::getScore)
				.filter(Score::isScoreSet)
				.sorted((e1, e2) -> e2.getScore() - e1.getScore())
				.sequential().collect(Collectors.toList());

		int entrySize = entries.size();
		int maxPage = entrySize % 9 == 0 ? entrySize / 9 : entrySize / 9 + 1;
		page = Math.min(page, maxPage);

		messages.rankingHeader(sender, objective.getName(), page, maxPage);

		for (int i = (page - 1) * 9; i < page * 9; i++) {
			messages.rankingFormat(sender, i + 1, entries.get(i).getEntry(), entries.get(i).getScore());
		}
		return true;
	}

	@Override
	public List<String> tabComplete(CommandSender sender, String[] args) {
		List<String> result = new ArrayList<>();
		Scoreboard mainScoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
		List<String> objectives = mainScoreboard.getObjectives().stream()
				.map(Objective::getName).collect(Collectors.toList());
		if (args.length == 2) {
			return StringUtil.copyPartialMatches(args[1], objectives, result);
		}

		if (!objectives.contains(args[1])) {
			return result;
		}

		int entrySize = (int) mainScoreboard.getEntries().parallelStream()
				.filter(entry -> mainScoreboard.getObjective(args[1]).getScore(entry).isScoreSet()).count();
		int maxPage = entrySize % 9 == 0 ? entrySize / 9 : entrySize / 9 + 1;

		if (args.length == 3) {
			List<String> pages = IntStream.rangeClosed(1, maxPage).boxed().map(String::valueOf).collect(Collectors.toList());
			return StringUtil.copyPartialMatches(args[2], pages, result);
		}

		return List.of();

	}
}
