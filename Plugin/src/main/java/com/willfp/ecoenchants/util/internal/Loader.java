package com.willfp.ecoenchants.util.internal;

import com.willfp.ecoenchants.EcoEnchantsPlugin;
import com.willfp.ecoenchants.command.commands.CommandEcodebug;
import com.willfp.ecoenchants.command.commands.CommandEcoreload;
import com.willfp.ecoenchants.command.commands.CommandEnchantinfo;
import com.willfp.ecoenchants.command.tabcompleters.TabCompleterEnchantinfo;
import com.willfp.ecoenchants.config.ConfigManager;
import com.willfp.ecoenchants.display.EnchantDisplay;
import com.willfp.ecoenchants.display.EnchantmentCache;
import com.willfp.ecoenchants.display.packets.PacketChat;
import com.willfp.ecoenchants.display.packets.PacketOpenWindowMerchant;
import com.willfp.ecoenchants.display.packets.PacketSetCreativeSlot;
import com.willfp.ecoenchants.display.packets.PacketSetSlot;
import com.willfp.ecoenchants.display.packets.PacketWindowItems;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentRarity;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentTarget;
import com.willfp.ecoenchants.enchantments.support.merging.anvil.AnvilListeners;
import com.willfp.ecoenchants.enchantments.support.merging.grindstone.GrindstoneListeners;
import com.willfp.ecoenchants.enchantments.support.obtaining.EnchantingListeners;
import com.willfp.ecoenchants.enchantments.support.obtaining.LootPopulator;
import com.willfp.ecoenchants.enchantments.support.obtaining.VillagerListeners;
import com.willfp.ecoenchants.enchantments.util.WatcherTriggers;
import com.willfp.ecoenchants.events.armorequip.ArmorListener;
import com.willfp.ecoenchants.events.armorequip.DispenserArmorListener;
import com.willfp.ecoenchants.events.entitydeathbyentity.EntityDeathByEntityListeners;
import com.willfp.ecoenchants.events.naturalexpgainevent.NaturalExpGainListeners;
import com.willfp.ecoenchants.integrations.anticheat.AnticheatManager;
import com.willfp.ecoenchants.integrations.anticheat.plugins.AnticheatAAC;
import com.willfp.ecoenchants.integrations.anticheat.plugins.AnticheatMatrix;
import com.willfp.ecoenchants.integrations.anticheat.plugins.AnticheatNCP;
import com.willfp.ecoenchants.integrations.anticheat.plugins.AnticheatSpartan;
import com.willfp.ecoenchants.integrations.antigrief.AntigriefManager;
import com.willfp.ecoenchants.integrations.antigrief.plugins.AntigriefFactionsUUID;
import com.willfp.ecoenchants.integrations.antigrief.plugins.AntigriefGriefPrevention;
import com.willfp.ecoenchants.integrations.antigrief.plugins.AntigriefKingdoms;
import com.willfp.ecoenchants.integrations.antigrief.plugins.AntigriefLands;
import com.willfp.ecoenchants.integrations.antigrief.plugins.AntigriefTowny;
import com.willfp.ecoenchants.integrations.antigrief.plugins.AntigriefWorldGuard;
import com.willfp.ecoenchants.integrations.essentials.EssentialsManager;
import com.willfp.ecoenchants.integrations.essentials.plugins.IntegrationEssentials;
import com.willfp.ecoenchants.integrations.mcmmo.McmmoManager;
import com.willfp.ecoenchants.integrations.mcmmo.plugins.McmmoIntegrationImpl;
import com.willfp.ecoenchants.integrations.placeholder.PlaceholderManager;
import com.willfp.ecoenchants.integrations.placeholder.plugins.PlaceholderIntegrationPAPI;
import com.willfp.ecoenchants.util.interfaces.Callable;
import com.willfp.ecoenchants.util.interfaces.EcoRunnable;
import com.willfp.ecoenchants.util.internal.updater.PlayerJoinListener;
import com.willfp.ecoenchants.util.internal.updater.UpdateChecker;
import com.willfp.ecoenchants.util.optional.Prerequisite;
import org.apache.maven.artifact.versioning.DefaultArtifactVersion;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Class containing methods for the loading and unloading of EcoEnchants
 */
public class Loader {

    /**
     * Called by {@link EcoEnchantsPlugin#onEnable()}
     */
    public static void load() {
        Logger.info("==========================================");
        Logger.info("");
        Logger.info("Loading &aEcoEnchants");
        Logger.info("Made by &aAuxilor&f - willfp.com");
        Logger.info("");
        Logger.info("==========================================");

        /*
        Load Configs
         */

        Logger.info("Loading Configs...");
        ConfigManager.updateConfigs();
        Logger.info("");

        /*
        Load ProtocolLib
         */

        Logger.info("Loading ProtocolLib...");
        if (ConfigManager.getConfig().getBool("villager.enabled")) {
            new PacketOpenWindowMerchant().register();
        }
        new PacketSetCreativeSlot().register();
        new PacketSetSlot().register();
        new PacketWindowItems().register();

        Logger.info("");

        /*
        Register Events
         */

        Logger.info("Registering Events...");
        Bukkit.getPluginManager().registerEvents(new ArmorListener(), EcoEnchantsPlugin.getInstance());
        Bukkit.getPluginManager().registerEvents(new DispenserArmorListener(), EcoEnchantsPlugin.getInstance());
        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(), EcoEnchantsPlugin.getInstance());
        Bukkit.getPluginManager().registerEvents(new EnchantingListeners(), EcoEnchantsPlugin.getInstance());
        Bukkit.getPluginManager().registerEvents(new GrindstoneListeners(), EcoEnchantsPlugin.getInstance());
        Bukkit.getPluginManager().registerEvents(new AnvilListeners(), EcoEnchantsPlugin.getInstance());
        Bukkit.getPluginManager().registerEvents(new EntityDeathByEntityListeners(), EcoEnchantsPlugin.getInstance());
        Bukkit.getPluginManager().registerEvents(new NaturalExpGainListeners(), EcoEnchantsPlugin.getInstance());
        Bukkit.getPluginManager().registerEvents(new VillagerListeners(), EcoEnchantsPlugin.getInstance());
        Bukkit.getPluginManager().registerEvents(new ArrowListeners(), EcoEnchantsPlugin.getInstance());
        Bukkit.getPluginManager().registerEvents(new WatcherTriggers(), EcoEnchantsPlugin.getInstance());
        Logger.info("");

        /*
        Load integrations
         */

        Logger.info("Loading Integrations...");

        final HashMap<String, Callable> integrations = new HashMap<String, Callable>() {{
            // AntiGrief
            put("WorldGuard", () -> AntigriefManager.register(new AntigriefWorldGuard()));
            put("GriefPrevention", () -> AntigriefManager.register(new AntigriefGriefPrevention()));
            put("FactionsUUID", () -> AntigriefManager.register(new AntigriefFactionsUUID()));
            put("Towny", () -> AntigriefManager.register(new AntigriefTowny()));
            put("Lands", () -> AntigriefManager.register(new AntigriefLands()));
            put("Kingdoms", () -> AntigriefManager.register(new AntigriefKingdoms()));

            // AntiCheat
            put("AAC", () -> AnticheatManager.register(new AnticheatAAC()));
            put("Matrix", () -> AnticheatManager.register(new AnticheatMatrix()));
            put("NoCheatPlus", () -> AnticheatManager.register(new AnticheatNCP()));
            put("Spartan", () -> AnticheatManager.register(new AnticheatSpartan()));

            // MISC
            put("Essentials", () -> EssentialsManager.register(new IntegrationEssentials()));
            put("PlaceholderAPI", () -> PlaceholderManager.addIntegration(new PlaceholderIntegrationPAPI()));
            put("mcMMO", () -> McmmoManager.registerIntegration(new McmmoIntegrationImpl()));
        }};

        Set<String> enabledPlugins = Arrays.stream(Bukkit.getPluginManager().getPlugins()).map(Plugin::getName).collect(Collectors.toSet());

        integrations.forEach(((s, callable) -> {
            StringBuilder log = new StringBuilder();
            log.append(s).append(": ");
            if (enabledPlugins.contains(s)) {
                callable.call();
                log.append("&aENABLED");
            } else {
                log.append("&9DISABLED");
            }
            Logger.info(log.toString());
        }));

        Prerequisite.update();
        Logger.info("");

        /*
        Create enchantment config files (for first time use)
         */

        Logger.info("Creating Enchantment Configs...");
        ConfigManager.updateEnchantmentConfigs();
        Logger.info("");

        /*
        Load all enchantments, rarities, and targets
         */

        Logger.info("Adding Enchantments to API...");
        EnchantmentRarity.update();
        EnchantmentTarget.update();
        if (EnchantmentRarity.getAll().size() == 0 || EnchantmentTarget.getAll().size() == 0) {
            Logger.error("&cError loading rarities or targets! Aborting...");
            Bukkit.getPluginManager().disablePlugin(EcoEnchantsPlugin.getInstance());
            return;
        } else {
            Logger.info(EnchantmentRarity.getAll().size() + " Rarities Loaded:");
            EnchantmentRarity.getAll().forEach((rarity -> {
                Logger.info("- " + rarity.getName() + ": Table Probability=" + rarity.getProbability() + ", Minimum Level=" + rarity.getMinimumLevel() + ", Villager Probability=" + rarity.getVillagerProbability() + ", Loot Probability=" + rarity.getLootProbability() + ", Has Custom Color=" + rarity.hasCustomColor());
            }));

            Logger.info("");

            Logger.info(EnchantmentTarget.getAll().size() + " Targets Loaded:");
            EnchantmentTarget.getAll().forEach((target) -> {
                Logger.info("- " + target.getName() + ": Materials=" + target.getMaterials().toString());
            });
        }
        Logger.info("");

        /*
        Load Extensions
         */

        Logger.info("Loading Extensions...");

        EcoEnchantsPlugin.getInstance().getExtensionLoader().loadExtensions();

        if (EcoEnchantsPlugin.getInstance().getExtensionLoader().getLoadedExtensions().isEmpty()) {
            Logger.info("&cNo extensions found");
        } else {
            Logger.info("Extensions Loaded:");
            EcoEnchantsPlugin.getInstance().getExtensionLoader().getLoadedExtensions().forEach((extension) -> {
                Logger.info("- " + extension.getName() + " v" + extension.getVersion());
            });
        }
        Logger.info("");

        if (EcoEnchants.getAll().size() == 0) {
            Logger.error("&cError adding enchantments! Aborting...");
            Bukkit.getPluginManager().disablePlugin(EcoEnchantsPlugin.getInstance());
            return;
        } else {
            Logger.info(EcoEnchants.getAll().size() + " Enchantments Loaded:");
            Logger.info(EcoEnchants.getAll().stream().map(ecoEnchant -> ecoEnchant.getType().getColor() + ecoEnchant.getName()).collect(Collectors.joining(", ")));
        }
        Logger.info("");

        /*
        Load enchantment configs
         */

        Logger.info("Loading Enchantment Configs...");
        ConfigManager.updateEnchantmentConfigs();
        Logger.info("");

        /*
        Register Enchantments
         */

        Logger.info("Registering Enchantments...");
        EcoEnchants.update();
        EnchantDisplay.update();
        Logger.info("");

        /*
        Register Enchantment Listeners
         */

        Logger.info("Registering Enchantment Listeners...");
        EcoEnchants.getAll().forEach((ecoEnchant -> {
            if (ecoEnchant.isEnabled()) {
                Bukkit.getPluginManager().registerEvents(ecoEnchant, EcoEnchantsPlugin.getInstance());
            }
        }));
        Logger.info("");

        /*
        Register Enchantment Tasks
         */

        Logger.info("Registering Enchantment Tasks...");
        EcoEnchants.getAll().forEach((ecoEnchant -> {
            if (ecoEnchant instanceof EcoRunnable) {
                Bukkit.getScheduler().scheduleSyncRepeatingTask(EcoEnchantsPlugin.getInstance(), (Runnable) ecoEnchant, 5, ((EcoRunnable) ecoEnchant).getTime());
            }
        }));
        Logger.info("");


        /*
        Load Commands
         */

        Logger.info("Loading Commands...");
        new CommandEcoreload().register();
        new CommandEcodebug().register();
        new CommandEnchantinfo().register();
        Logger.info("");
        
        /*
        Start bStats
         */

        Logger.info("Hooking into bStats...");
        new Metrics(EcoEnchantsPlugin.getInstance(), 7666);
        Logger.info("");

        /*
        Finish
         */

        Bukkit.getScheduler().runTaskLater(EcoEnchantsPlugin.getInstance(), Loader::postLoad, 1);

        Logger.info("Loaded &aEcoEnchants!");
    }

    /**
     * Called after server is loaded
     */
    public static void postLoad() {
        Logger.info("Adding block populators...");

        Bukkit.getServer().getWorlds().forEach((world -> {
            world.getPopulators().add(new LootPopulator());
        }));

        Logger.info("");

        new UpdateChecker(EcoEnchantsPlugin.getInstance(), 79573).getVersion((version) -> {
            DefaultArtifactVersion currentVersion = new DefaultArtifactVersion(EcoEnchantsPlugin.getInstance().getDescription().getVersion());
            DefaultArtifactVersion mostRecentVersion = new DefaultArtifactVersion(version);
            if (!(currentVersion.compareTo(mostRecentVersion) > 0 || currentVersion.equals(mostRecentVersion))) {
                UpdateChecker.setOutdated(true);
                UpdateChecker.setNewVersion(version);
                Bukkit.getScheduler().runTaskTimer(EcoEnchantsPlugin.getInstance(), () -> {
                    Logger.info("&cEcoEnchants is out of date! (Version " + EcoEnchantsPlugin.getInstance().getDescription().getVersion() + ")");
                    Logger.info("&cThe newest version is &f" + version);
                    Logger.info("&cDownload the new version here:&f https://www.spigotmc.org/resources/ecoenchants.79573/");
                }, 0, 864000);
            }
            Logger.info("");
            Logger.info("----------------------------");
        });

        /*
        Check for paper
         */

        if (!Prerequisite.HasPaper.isMet()) {
            Logger.error("");
            Logger.error("----------------------------");
            Logger.error("");
            Logger.error("You don't seem to be running paper!");
            Logger.error("Paper is strongly recommended for all servers,");
            Logger.error("and some enchantments may not function properly without it");
            Logger.error("Download Paper from &fhttps://papermc.io");
            Logger.error("");
            Logger.error("----------------------------");
            Logger.error("");
        }

        Logger.info("");

        Logger.info("Updating cache...");
        new PacketChat().register();
        EcoEnchants.getAll().forEach(EcoEnchant::update);
        EnchantmentCache.update();
        EssentialsManager.registerEnchantments();
        Logger.info("");
    }

    /**
     * Called by {@link EcoEnchantsPlugin#onDisable()}
     */
    public static void unload() {
        Logger.info("&cDisabling EcoEnchants...");
        Logger.info("Removing Block Populators...");
        Bukkit.getServer().getWorlds().forEach((world -> {
            List<BlockPopulator> populators = new ArrayList<>(world.getPopulators());
            populators.forEach((blockPopulator -> {
                if (blockPopulator instanceof LootPopulator) {
                    world.getPopulators().remove(blockPopulator);
                }
            }));
        }));
        Logger.info("");
        Logger.info("&cUnloading Extensions...");
        EcoEnchantsPlugin.getInstance().getExtensionLoader().unloadExtensions();
        Logger.info("&fBye! :)");
    }

    /**
     * Called by /ecoreload
     */
    public static void reload() {
        ConfigManager.updateConfigs();
        EnchantmentRarity.update();
        EnchantmentTarget.update();
        EcoEnchants.update();
        EnchantDisplay.update();
        TabCompleterEnchantinfo.reload();
        EcoEnchant.EnchantmentType.update();

        EcoEnchants.getAll().forEach((ecoEnchant -> {
            HandlerList.unregisterAll(ecoEnchant);

            Bukkit.getScheduler().runTaskLater(EcoEnchantsPlugin.getInstance(), () -> {
                if (ecoEnchant.isEnabled()) {
                    Bukkit.getPluginManager().registerEvents(ecoEnchant, EcoEnchantsPlugin.getInstance());
                }
            }, 1);
        }));
    }
}
