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
* EnabledByDefault: Enable the plugin for all users, ignoring any permission plugin. (Default: false)
* GoodPing: Pings below this number will be printed as green in the /ping command. (Default: 200)
* MediumPing: Pings below this number will be printed as gold in the /ping command. If the ping gets above this number, it will be printed in red. (Default: 500)
* AlertPlayers: Alert players about high latency (Default: true)
* AlertThreshold: Players with the ping higher than this will be alerted (Default: 500)
* AlertInterval: The interval, in minutes, for the alerts to be sent (Default: 5)
* AlertMessage: The message that will be sent to the user on the alert event. Color codes are supported using the & character. Parameters: %playername - Player Name, %ping - Ping Measured, %threshold - Threshold Set. (Default: "%playername, your latency of %ping is above %threshold!")


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

Won't Work
=======
* Configurable and colored measures: Unfortunately that isn't possible at the moment.
* ICMP latency measurements: Java doesn't support ICMP, there's a workaround but it too messy.

![PingTab MCStats](http://api.mcstats.org/signature/PingTab.png)
