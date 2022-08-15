package fr.av.codelyokouhc;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.sk89q.worldedit.CuboidClipboard;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.schematic.MCEditSchematicFormat;
import com.sk89q.worldedit.world.DataException;
import fr.av.codelyokouhc.commands.*;
import fr.av.codelyokouhc.enums.GRoles;
import fr.av.codelyokouhc.enums.GState;
import fr.av.codelyokouhc.listeners.DammageListeners;
import fr.av.codelyokouhc.listeners.HealthListeners;
import fr.av.codelyokouhc.listeners.PlayerListeners;
import fr.av.codelyokouhc.loops.AnswerLoop;
import fr.av.codelyokouhc.loops.GameLoop;
import fr.av.codelyokouhc.loops.RemoveKilledPlayerLoop;
import fr.av.codelyokouhc.loops.RolesLoop;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class Main extends JavaPlugin {
    private GState state;
    private Location gameSpawn;
    private Location lyokoSpawn;
    private Map<Player, GRoles> roles = new HashMap<>();
    private List<GRoles> nonAttribuateRoles = new ArrayList<>();
    private Map<Player, Location> inLyokoPlayer = new HashMap<>();
    private int episode = 0;
    public GameLoop gameLoop = null;
    public RolesLoop rolesLoop = null;
    public int worldBorder = 1500;

    //Reset when episode change
    private boolean jeremyCanVanish = true;

    public int ulrichHide = 10;
    public int jeremyRevive = 2;
    public boolean tamiyaAsk = true;
    public Player tamiyaTarget = null;
    public AnswerLoop tamiyaAnswerLoop = null;
    public Map<Player, Location> killedPlayer = new HashMap<>();
    public Player ulrichGirl = null;
    public List<Player> kankrelats = new ArrayList<>();
    public List<Player> spectres = new ArrayList<>();
    public Map<Player,Float> blocks = new HashMap<>();
    public boolean franzCanInfect = false;
    public Player franzInfected = null;
    public boolean aelitaCanJump = true;
    public boolean computerWork = false;
    public boolean millyCanShow = true;
    public Map<Player, Integer> lyokoBoostedPlayer = new HashMap<>();
    public int scanCount = 0;

    @Override
    public void onEnable() {
        super.onEnable();
        System.out.println("Code Lyoko est pret !");
        if(Bukkit.getAllowNether()) System.out.println("/!\\WARNING/!\\ Nether is enable !");

        getCommand("tpspawn").setExecutor(new TpSpawnCommand());
        getCommand("startGame").setExecutor(new StartGameCommand(this));
        getCommand("setGameSpawn").setExecutor(new SetGameSpawnCommand(this));
        getCommand("setLyokoSpawn").setExecutor(new SetLyokoSpawnCommand(this));
        getCommand("getRole").setExecutor(new GetRoleCommand(this));
        getCommand("lyokoTp").setExecutor(new LyokoTp(this));
        getCommand("spawn").setExecutor(new SpawnCommand(this));
        getCommand("cl").setExecutor(new CLCommand(this));
        getCommand("checkWin").setExecutor(new CheckWinCommand(this));
        getCommand("setBorder").setExecutor(new SetBorderCommand(this));
        getCommand("config").setExecutor(new ConfigCommand(this));

        Location factorySpawn = new Location(getServer().getWorld("world"), new Random().nextInt(500 - (-500)) + (-500), 0, new Random().nextInt(500 - (-500)) + (-500));
        int y = factorySpawn.getWorld().getHighestBlockYAt(factorySpawn);
        factorySpawn = new Location(factorySpawn.getWorld(), factorySpawn.getX(), y, factorySpawn.getZ());
        System.out.println(factorySpawn);
        generateFactory(factorySpawn);

        Bukkit.createWorld(new WorldCreator("lyoko"));

        setWorldBorder(worldBorder);

        setState(GState.WAITINGPLAYERS);
        nonAttribuateRoles.add(GRoles.AelitaSchaeffer);
        nonAttribuateRoles.add(GRoles.ChefDuXana);
        nonAttribuateRoles.add(GRoles.JeanPierreDelmas);
        nonAttribuateRoles.add(GRoles.JeremyBelpois);
        nonAttribuateRoles.add(GRoles.JimMoralés);
        nonAttribuateRoles.add(GRoles.MillySolovieff);
        nonAttribuateRoles.add(GRoles.TamiyaDiop);
        nonAttribuateRoles.add(GRoles.Odd);
        nonAttribuateRoles.add(GRoles.Kiwi);
        nonAttribuateRoles.add(GRoles.Hervé);
        nonAttribuateRoles.add(GRoles.Nicolas);
        nonAttribuateRoles.add(GRoles.Sisi);
        nonAttribuateRoles.add(GRoles.SuzanneHertz);
        nonAttribuateRoles.add(GRoles.UlrichStern);
        nonAttribuateRoles.add(GRoles.WilliamDunba);
        nonAttribuateRoles.add(GRoles.YumiIshiyama);
        nonAttribuateRoles.add(GRoles.MèreDeYumi);
        nonAttribuateRoles.add(GRoles.PèreDeYumi);

        ItemStack pinkSword = new ItemStack(Material.GOLD_SWORD);
        pinkSword.addUnsafeEnchantment(Enchantment.DURABILITY, 100);
        pinkSword.addEnchantment(Enchantment.DAMAGE_ALL,4);
        pinkSword.addEnchantment(Enchantment.KNOCKBACK,1);
        ItemMeta pinkSwordMeta = pinkSword.getItemMeta();
        pinkSwordMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        pinkSwordMeta.setDisplayName("Lame Rose");
        pinkSword.setItemMeta(pinkSwordMeta);

        ItemStack samurailHelmet = new ItemStack(Material.GOLD_HELMET);
        samurailHelmet.addUnsafeEnchantment(Enchantment.DURABILITY, 100);
        samurailHelmet.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4);
        ItemMeta samurailHelmetItemMeta = samurailHelmet.getItemMeta();
        samurailHelmetItemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        samurailHelmetItemMeta.setDisplayName("Samurail Helmet");
        samurailHelmet.setItemMeta(samurailHelmetItemMeta);

        ItemStack samurailChestplate = new ItemStack(Material.GOLD_CHESTPLATE);
        samurailChestplate.addUnsafeEnchantment(Enchantment.DURABILITY, 100);
        samurailChestplate.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4);
        ItemMeta samurailChestplateItemMeta = samurailChestplate.getItemMeta();
        samurailChestplateItemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        samurailChestplateItemMeta.setDisplayName("Samurail Chestplate");
        samurailChestplate.setItemMeta(samurailChestplateItemMeta);

        ItemStack samurailLeggins = new ItemStack(Material.GOLD_LEGGINGS);
        samurailLeggins.addUnsafeEnchantment(Enchantment.DURABILITY, 100);
        samurailLeggins.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4);
        ItemMeta samurailLegginsItemMeta = samurailLeggins.getItemMeta();
        samurailLegginsItemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        samurailLegginsItemMeta.setDisplayName("Samurail Leggings");
        samurailLeggins.setItemMeta(samurailLegginsItemMeta);

        ItemStack samurailBoots = new ItemStack(Material.GOLD_BOOTS);
        samurailBoots.addUnsafeEnchantment(Enchantment.DURABILITY, 100);
        samurailBoots.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4);
        ItemMeta samurailBootsItemMeta = samurailBoots.getItemMeta();
        samurailBootsItemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        samurailBootsItemMeta.setDisplayName("Samurail Boots");
        samurailBoots.setItemMeta(samurailBootsItemMeta);

        ShapedRecipe pinkSwordRecipe =  new ShapedRecipe(pinkSword);
        pinkSwordRecipe.shape("OOO","ODO","OOO");
        pinkSwordRecipe.setIngredient('O', Material.OBSIDIAN);
        pinkSwordRecipe.setIngredient('D', Material.DIAMOND);
        getServer().addRecipe(pinkSwordRecipe);

        ShapedRecipe samurailHelmetRecipe =  new ShapedRecipe(samurailHelmet);
        samurailHelmetRecipe.shape("III","G G");
        samurailHelmetRecipe.setIngredient('I', Material.IRON_INGOT);
        samurailHelmetRecipe.setIngredient('G', Material.GOLDEN_APPLE);
        getServer().addRecipe(samurailHelmetRecipe);

        ShapedRecipe samurailChestplateRecipe =  new ShapedRecipe(samurailChestplate);
        samurailChestplateRecipe.shape("D D","IDI","III");
        samurailChestplateRecipe.setIngredient('D', Material.DIAMOND);
        samurailChestplateRecipe.setIngredient('I', Material.IRON_INGOT);
        getServer().addRecipe(samurailChestplateRecipe);

        ShapedRecipe samurailLeggingsRecipe =  new ShapedRecipe(samurailLeggins);
        samurailLeggingsRecipe.shape("III","I I","D D");
        samurailLeggingsRecipe.setIngredient('D', Material.DIAMOND);
        samurailLeggingsRecipe.setIngredient('I', Material.IRON_INGOT);
        getServer().addRecipe(samurailLeggingsRecipe);

        ShapedRecipe samurailBootsRecipe =  new ShapedRecipe(samurailBoots);
        samurailBootsRecipe.shape("I I","G G");
        samurailBootsRecipe.setIngredient('G', Material.GOLDEN_APPLE);
        samurailBootsRecipe.setIngredient('I', Material.IRON_INGOT);
        getServer().addRecipe(samurailBootsRecipe);

        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new PlayerListeners(this), this);
        pm.registerEvents(new DammageListeners(this), this);
        pm.registerEvents(new HealthListeners(this), this);
    }

    @Override
    public void onDisable() {
        super.onDisable();
        System.out.println("Code Lyoko est eteint !");
    }

    public void setState(GState state) {
        this.state = state;
    }
    public boolean isState(GState state){
        return this.state == state;
    }
    public Location getGameSpawn() {
        return gameSpawn;
    }
    public void setGameSpawn(Location gameSpawn) {
        this.gameSpawn = gameSpawn;
    }
    public Map<Player, GRoles> getRoles() {
        return roles;
    }
    public List<GRoles> getNonAttribuateRoles() {
        return nonAttribuateRoles;
    }
    public Location getLyokoSpawn() {
        return lyokoSpawn;
    }
    public void setLyokoSpawn(Location lyokoSpawn) {
        this.lyokoSpawn = lyokoSpawn;
    }
    public int getEpisode() {
        return episode;
    }
    public void setEpisode(int episode) {
        this.episode = episode;
    }
    public boolean jeremyCanVanish() {
        return jeremyCanVanish;
    }
    public void setJeremyCanVanish(boolean jeremyCanVanish) {
        this.jeremyCanVanish = jeremyCanVanish;
    }

    public void eliminate(Player player){
        killedPlayer.put(player, player.getLocation());
        RemoveKilledPlayerLoop rkpl = new RemoveKilledPlayerLoop(this, player);
        rkpl.runTaskTimer(this, 1,20);
        DropInventory(player);
        player.setGameMode(GameMode.SPECTATOR);
        for (Player p : getServer().getOnlinePlayers()) {
            p.playSound(p.getLocation(), Sound.WITHER_SPAWN, 1.0f, 1.0f);
        }
        Bukkit.broadcastMessage("§c====================");
        if(roles.containsKey(player)){
            Bukkit.broadcastMessage("§e" + player.getDisplayName() + " a été tué ! Il était " + roles.get(player).toString());
        }else{
            Bukkit.broadcastMessage("§e" + player.getDisplayName() + " est mort");
        }
        Bukkit.broadcastMessage("§c====================");

        //Special DeathEvents
        if(roles.get(player) == GRoles.Odd && getPlayerByRole(GRoles.Kiwi) != null && getPlayerByRole(GRoles.Kiwi).getGameMode() == GameMode.SURVIVAL){
            Player kiwi = getPlayerByRole(GRoles.Kiwi);
            kiwi.setMaxHealth(16);
            kiwi.setHealth(16);
            kiwi.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 999999999,0, false, false));
            getPlayerByRole(GRoles.Kiwi).sendMessage("§eOdd est mort, votre boussole pointe désormais le 0 0");
        }
        if(roles.get(player) == GRoles.Kiwi && getPlayerByRole(GRoles.Odd) != null && getPlayerByRole(GRoles.Odd).getGameMode() == GameMode.SURVIVAL){
            if(playerIsAt(player, getPlayerByRole(GRoles.Odd), 30)) getPlayerByRole(GRoles.Odd).addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 999999999,1, false, false));
        }
        if(roles.get(player) == GRoles.YumiIshiyama){
            if(getPlayerByRole(GRoles.PèreDeYumi) == null && getPlayerByRole(GRoles.PèreDeYumi).getGameMode() == GameMode.SURVIVAL) eliminate(getPlayerByRole(GRoles.PèreDeYumi));
            if(getPlayerByRole(GRoles.MèreDeYumi) == null && getPlayerByRole(GRoles.MèreDeYumi).getGameMode() == GameMode.SURVIVAL) eliminate(getPlayerByRole(GRoles.PèreDeYumi));
        }

        if(roles.get(player) == GRoles.MèreDeYumi || roles.get(player) == GRoles.PèreDeYumi){
            if(getPlayerByRole(GRoles.YumiIshiyama) != null && getPlayerByRole(GRoles.YumiIshiyama).getGameMode() == GameMode.SURVIVAL) {
                Player yumi = getPlayerByRole(GRoles.YumiIshiyama);
                yumi.setMaxHealth(yumi.getMaxHealth() - 6);
                yumi.setHealth(yumi.getMaxHealth());
            }
        }

        if(player == ulrichGirl && getPlayerByRole(GRoles.UlrichStern) != null){
            Player us = getPlayerByRole(GRoles.UlrichStern);
            us.sendMessage("§cVotre amante est morte. Votre tristesse vous fait tomber à 4 coeurs !");
            us.setMaxHealth(8);
            us.setHealth(8);
        }

        if(roles.get(player) == GRoles.UlrichStern && ulrichGirl != null){
            ulrichGirl.sendMessage("§cVotre amant est mort. Votre tristesse vous fait tomber à 4 coeurs !");
            ulrichGirl.setMaxHealth(8);
            ulrichGirl.setHealth(8);
        }
        CheckWin();
    }

    public void DropInventory(Player player){
        try
        {
            for (ItemStack itemStack : player.getInventory().getArmorContents()) {
                if(itemStack != null && itemStack.getType() != Material.AIR){
                    player.getWorld().dropItemNaturally(player.getLocation(), itemStack);
                }
            }
            for (ItemStack itemStack : player.getInventory().getContents()) {
                if(itemStack != null && itemStack.getType() != Material.AIR){
                    player.getWorld().dropItemNaturally(player.getLocation(), itemStack);
                }
            }
            player.getWorld().dropItemNaturally(player.getLocation(), new ItemStack(Material.GOLDEN_APPLE));
        }
        catch(IllegalArgumentException e)
        {
            e.printStackTrace();
        }
    }

    public void setWorldBorder(int dist){
        WorldBorder border = getServer().getWorld("world").getWorldBorder();
        border.setSize(dist*2);
        border.setDamageAmount(2);
        border.setDamageBuffer(2);
        border.setCenter(0,0);
    }

    public Boolean playerIsAt(Player player1, Player player2, float dist){
        return player2.getLocation().distanceSquared(player1.getLocation()) <= (dist * dist);
    }

    public <V> Player getPlayerByRole(V value) {
        Supplier<Stream<Object>> result = () -> getRoles()
                .entrySet()
                .stream()
                .filter(entry -> value.equals(entry.getValue()))
                .map(Map.Entry::getKey);

        if(result.get().count() > 0) return (Player) result.get().findFirst().get();
        else return null;
    }

    public boolean isDay(){
        return getServer().getWorld("world").getTime() < 12300 || getServer().getWorld("world").getTime() > 23850;
    }

    public String getPlayerCount() {
        int count = 0;
        for (Player player : getServer().getOnlinePlayers()) {
            if(player.getGameMode() != GameMode.SPECTATOR){
                count++;
            }
        }
        return String.valueOf(count);
    }

    public void addPlayerLyoko(Player player){
        if(!inLyokoPlayer.containsKey(player)) inLyokoPlayer.put(player, player.getLocation());
    }

    public void removePlayerLyoko(Player player){
        if(inLyokoPlayer.containsKey(player)) inLyokoPlayer.remove(player);
    }

    public boolean isInLyoko(Player player){
        if(inLyokoPlayer.containsKey(player)) return true;
        return false;
    }

    public Location getOverworldSpawnPlayer(Player player){
        if(inLyokoPlayer.containsKey(player)) return inLyokoPlayer.get(player).add(2,2,2);
        else return null;
    }

    public WorldEditPlugin getWorldEdit(){
        Plugin p = Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");
        if(p instanceof WorldEditPlugin) return (WorldEditPlugin) p;
        else return null;
    }

    private void generateFactory(Location location){
        WorldEditPlugin worldEdit = getWorldEdit();
        File shematic = new File(getDataFolder(), "factory.schematic");
        EditSession session = worldEdit.getWorldEdit().getEditSessionFactory().getEditSession(new BukkitWorld(location.getWorld()), 999999);
        try{
            CuboidClipboard clipboard = MCEditSchematicFormat.getFormat(shematic).load(shematic);
            clipboard.rotate2D(90);
            clipboard.paste(session, new Vector(location.getX(),location.getY(), location.getZ()), false);
        }catch (MaxChangedBlocksException | DataException | IOException e){
            e.printStackTrace();
        }
    }

    public ItemStack getSkullWithUrl(String url) {
        ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);

        if (url == null || url.isEmpty()) return skull;

        ItemMeta skullMeta = skull.getItemMeta();
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        byte[] encodedData = Base64.getEncoder().encode(String.format("{textures:{SKIN:{url:\"%s\"}}}", url).getBytes());
        profile.getProperties().put("textures", new Property("textures", new String(encodedData)));

        Field profileField = null;
        try {
            profileField = skullMeta.getClass().getDeclaredField("profile");
        } catch (NoSuchFieldException | SecurityException e) {
            e.printStackTrace();
        }
        profileField.setAccessible(true);
        try {
            profileField.set(skullMeta, profile);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }
        skull.setItemMeta(skullMeta);
        return skull;
    }

    public void sendMessageInfo(Player player, GRoles role){
        String msg = "";
        switch (role){
            case AelitaSchaeffer:
                msg = "§bVous êtes un Lyoko Guerrier. Vous avez un double jump toutes les 30s";
                break;
            case ChefDuXana:
                msg = "§bVous êtes le chef du XANA. Vous devez retrouver vos agents et gagner avec eux. Pour les aider vous avez accès au commandes /cl blocks, /cl MegaTanks, /cl kankrelats, /cl spectre (/cl help pour plus d'info)";
                break;
            case JeanPierreDelmas:
                msg = "§bVous êtes un Lyoko Guerrier. Lorsque vous êtes entouré de plus de 3 de vos alliés, vous obtenez speed 1.";
                break;
            case JeremyBelpois:
                msg = "§bVous êtes un Lyoko Guerrier. Grace à la commande /cl revive vous pouvez revive toutes les personnes mortes durant les 60 dernières secondes et cela deux fois dans la game. De plus, vous avez l'item supercalculateur qui vous permet de vous rendre dans le lyoko n'importe quand à partir de 60 minutes de jeux.";
                break;
            case JimMoralés:
                msg = "§bVous êtes un Lyoko Guerrier. Tous les 3000 blocs parcourus vous gagner du speed suplémentaire.";
                break;
            case MillySolovieff:
                msg = "§bVous êtes un Lyoko Guerrier. Vous êtes un rôle à info. Grace à votre item échos de kadic vous pouvez voir la moitié de l'inventaire d'un joueur toutes les 13 minutes.";
                break;
            case TamiyaDiop:
                msg = "§bVous êtes un Lyoko Guerrier. Grace à la commande /cl ask vous pouvez poser une question à n'importe quel joueur de la partie qui devra vous répondre par oui ou par non.";
                break;
            case Odd:
                msg = "§bVous êtes en duo avec kiwi. A coté de lui vous obtenez forece 1 de jour et résistance 1 la nuit. Si kiwi vient à mourir avant vous et qu'il est dans un périmètre de 30 blocs vous obtiendrez une résistance 2 ";
                break;
            case Kiwi:
                msg = "§bVous êtes en duo avec Odd. Si odd meurs avant vous, vous vous retrouvez à 8 coeurs et weakness";
                break;
            case Hervé:
            case Nicolas:
            case SuzanneHertz:
            case UlrichStern:
                msg = "§bVous êtes un Lyoko Guerrier.";
                break;
            case Sisi:
                msg = "§bVous êtes un Lyoko Guerrier. Vous pouvez crafter la Lame Rose qui est une arme avec 1 bloc de plus de portée enchanté sharpness 1 et knockback 1";
                break;
            case WilliamDunba:
                msg = "§bVous êtes un Lyoko Guerrier. Vous pouvez vous faire infecter par Franz si il reste plus de 4 minutes à coté de vous. Vous rejoindrez alors son clan avec Résistance 1 Speed 1 Force 1 et Agilité Aquatique";
                break;
            case YumiIshiyama:
                msg = "§bVous êtes en équipe avec vos parents. Vous devez gagner avec eux. Vous perdrez 3 coeurs permanent a chaque fois que l'un des deux sucombra.";
                break;
            case MèreDeYumi:
            case PèreDeYumi:
                msg = "§bVous êtes en équipe avec Yumi. Vous devez la protéger au péril de votre vie. Votre bousole pointe vers elle à tous moments !";
                break;
            case Kalamar:
                msg = "§bVous êtes en équipe avec le chef du XANA. Vous devez l'aider à trouver ses agents. Il connais votre pseudo.";
                break;
            case Agent:
                msg = "§bVous êtes en équipe avec le chef du XANA. Vous devez le retrouver pour qu'il vous accorde des pouvoirs.";
                break;
        }
        player.sendMessage(msg);
    }
    public void CheckWin(){
        int lyoko = 0;
        int XANA = 0;
        int OddKiwi = 0;
        int Yumi = 0;

        for (Player player : getServer().getOnlinePlayers()) {
            if(player.getGameMode() == GameMode.SURVIVAL){
                GRoles role = roles.get(player);
                switch (role){
                    case AelitaSchaeffer:
                    case JeanPierreDelmas:
                    case JeremyBelpois:
                    case JimMoralés:
                    case MillySolovieff:
                    case TamiyaDiop:
                    case Hervé:
                    case Nicolas:
                    case Sisi:
                    case SuzanneHertz:
                    case UlrichStern:
                        lyoko ++;
                        break;
                    case ChefDuXana:
                    case Kalamar:
                    case ChefMegaTank:
                    case Agent:
                        XANA++;
                        break;
                    case Odd:
                    case Kiwi:
                        OddKiwi++;
                        break;
                    case WilliamDunba:
                        if(franzInfected != null) XANA++;
                        else lyoko++;
                        break;
                    case YumiIshiyama:
                    case MèreDeYumi:
                    case PèreDeYumi:
                        Yumi++;
                        break;
                }
            }
        }
        if(lyoko > 0 && XANA ==0 && OddKiwi == 0 && Yumi ==0) Win("Lyoko");
        else if(lyoko ==0 && XANA > 0 && OddKiwi == 0 && Yumi == 0) Win("XANA");
        else if(lyoko ==0 && XANA == 0 && OddKiwi > 0 && Yumi == 0) Win("Odd/Kiwi");
        else if(lyoko ==0 && XANA == 0 && OddKiwi == 0 && Yumi > 0) Win("Yumi");
    }

    public void Win(String winers){
        for (Player player : roles.keySet()) {
            if(player.getGameMode() != GameMode.SURVIVAL){
                Bukkit.broadcastMessage("§c" + player.getDisplayName() + " : " + roles.get(player).toString());
            }else{
                Bukkit.broadcastMessage("§b" + player.getDisplayName() + " : " + roles.get(player).toString());
            }
        }
        switch (winers){
            case "Lyoko":
                Bukkit.broadcastMessage("§aVictoire des Guerriers du Lyoko");
                break;
            case "XANA":
                Bukkit.broadcastMessage("§aVictoire des membres du XANA");
                break;
            case "Odd/Kiwi":
                boolean kiwi = getPlayerByRole(GRoles.Kiwi) != null;
                boolean odd = getPlayerByRole(GRoles.Odd) != null;
                if(kiwi && odd) Bukkit.broadcastMessage("§aVictoire de Odd et de Kiwi");
                else if(kiwi) Bukkit.broadcastMessage("§aVictoire de Kiwi");
                else Bukkit.broadcastMessage("§aVictoire de Odd");
                break;
            case "Yumi":
                boolean yumi = getPlayerByRole(GRoles.YumiIshiyama) != null;
                boolean m = getPlayerByRole(GRoles.MèreDeYumi) != null;
                boolean p = getPlayerByRole(GRoles.PèreDeYumi) != null;
                if(yumi && m && p) Bukkit.broadcastMessage("§aVictoire de Yumi et ses parents");
                else if(yumi && m && !p) Bukkit.broadcastMessage("§aVictoire de Yumi et sa mère");
                else if(yumi && !m && p) Bukkit.broadcastMessage("§aVictoire de Yumi et son père");
                else if(yumi && !m && !p) Bukkit.broadcastMessage("§aVictoire de Yumi");
                else Bukkit.broadcastMessage("§aVictoire des parents de Yumi");
                break;
        }
        for (Player player : getServer().getOnlinePlayers()) {
            player.playSound(player.getLocation(), Sound.LEVEL_UP,1f,1f);
        }
        gameLoop.UpdateScoreBoard();
        gameLoop.cancel();
        rolesLoop.cancel();
        state = GState.FINISH;
    }
}
