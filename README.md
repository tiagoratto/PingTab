![PingTab](http://dev.bukkit.org/media/images/67/252/PINGTAB_logo.png)
=======

Show players latency on the tablist

Description
=======
Since PingList doesn't seems to be maintained anymore, I've decided to implement my own version, much thanks for @Dutch1ee for the idea and @Ozelo for pushing me into making this.

Configuration
=======
On config.yml you can set:
* Interval: Pooling interval in seconds. (Default: 3)
* DisableTab: Disable the tab portion of the plugin. (Default: true)
* GoodPing: Pings below this number will be printed as green in the /ping command. (Default: 200)
* MediumPing: Pings below this number will be printed as gold in the /ping command. If the ping gets above this number, it will be printed in red. (Default: 500)
* OwnPingMessage: Message that is sent to the player when he check it's own ping with /ping. Color codes are supported using & character. Parameters: %ping - Ping Measured (Default: "Your ping is %pingms")
* PingMessage: Message that is sent to player when he check anothe player's  ping with /ping. Color codes are supported using & character. Parameters: %playername - Player Name; %ping - Ping Measured (Default: "%playername's ping is %pingms")
* AlertPlayers: Alert players about high latency (Default: true)
* AlertThreshold: Players with the ping higher than this will be alerted (Default: 1000)
* AlertInterval: The interval, in minutes, for the alerts to be sent (Default: 5)
* AlertMessage: The message that will be sent to the user on the alert event. Color codes are supported using the & character. Parameters: %playername - Player Name; %ping - Ping Measured; %threshold - Threshold Set. (Default: "%playername, your latency of %ping is above %threshold!")


Current Features
=======
* Shows latency on the tablist
* Support for colored names (This means that if you use a plugin to make the names colored it will work just fine)
* /ping [player|self] command
* High latency alerts
* SuperPerms Support

Planned Features
=======
* Autokick based on measured latency
* List players on tab based on radius and or limits for big servers
* List players order by own faction, enemy factions, other factions and lonewolves
* PermissionsEx Support
* Group Manager Support
* Multiworld support and scoreboard isolation
* Alert messages and kick based in the median ping, instead of last measured ping

Won't Work
=======
* Configurable and colored measures: Unfortunately that isn't possible at the moment.
* ICMP latency measurements: Java doesn't support ICMP, there's a workaround but it too messy.

![PingTab MCStats](http://api.mcstats.org/signature/PingTab.png)
