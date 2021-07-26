package com.c0ckp1t.content

import com.c0ckp1t.annotations.C0ckp1tMethod
import com.c0ckp1t.engine.KotlinScriptUtils
import com.c0ckp1t.engine.ScriptCtx
import com.c0ckp1t.utils.WfResult
import com.c0ckp1t.utils.readPropertiesFile
import java.io.File
import kotlinx.coroutines.flow.*


/**



sudo nmap -sC -sV -O 129.204.146.194
[sudo] password for luis:

Starting Nmap 7.60 ( https://nmap.org ) at 2020-07-28 21:29 CDT
Nmap scan report for 129.204.146.194
Host is up (0.23s latency).
Not shown: 984 closed ports
PORT     STATE    SERVICE       VERSION










21/tcp   open     ftp           FileZilla ftpd
| ftp-syst:
|_  SYST: UNIX emulated by FileZilla
80/tcp   open     http          nginx 1.15.11
|_http-server-header: nginx/1.15.11
|_http-title: Site doesn't have a title (text/html; charset=utf-8).
81/tcp   open     http          nginx 1.15.11
|_http-server-header: nginx/1.15.11
|_http-title: 404 Not Found
82/tcp   open     http          nginx 1.15.11
|_http-server-header: nginx/1.15.11
83/tcp   open     http          nginx 1.15.11
|_http-title: 404 Not Found
135/tcp  open     msrpc         Microsoft Windows RPC
139/tcp  open     netbios-ssn   Microsoft Windows netbios-ssn
445/tcp  filtered microsoft-ds
1025/tcp open     msrpc         Microsoft Windows RPC
1026/tcp open     msrpc         Microsoft Windows RPC
1027/tcp open     msrpc         Microsoft Windows RPC
1029/tcp open     msrpc         Microsoft Windows RPC
1035/tcp open     msrpc         Microsoft Windows RPC
3306/tcp open     mysql         MySQL 5.7.26
| mysql-info:
|   Protocol: 10
|   Version: 5.7.26
|   Thread ID: 14463
|   Capabilities flags: 63487
|   Some Capabilities: ConnectWithDatabase, FoundRows, LongPassword, SupportsCompression, SupportsLoadDataLocal, Support41Auth, IgnoreSigpipes, Speaks41ProtocolOld, Speaks41ProtocolNew, SupportsTransactions, ODBCClient, DontAllowDatabaseTableColumn, IgnoreSpaceBeforeParenthesis, LongColumnFlag, InteractiveClient, SupportsMultipleResults, SupportsAuthPlugins, SupportsMultipleStatments
|   Status: Autocommit
|   Salt: bQ|\x05\x08\x14\x07w*\x06?%\x1E{\x05+UM1\x1E
|_  Auth Plugin Name: 79
3389/tcp open     ms-wbt-server Microsoft Terminal Service
|_ssl-date: 2020-07-29T02:41:23+00:00; 0s from scanner time.
4444/tcp filtered krb524
Aggressive OS guesses: Microsoft Windows Server 2008 R2 (92%), Microsoft Windows 8.1 Enterprise (92%), Linux 2.6.23-gentoo-r3 (92%), Samsung OfficeServ 7100 VoIP adapter (92%), Yealink SIP-T22P VoIP phone (92%), Linux 2.4.21 - 2.4.31 (likely embedded) (92%), Linux 2.6.18 (Debian 4.0, x86) (92%), Linux 2.6.18 - 2.6.24 (92%), Linux 2.6.22 (92%), MikroTik RouterOS 3.17 (92%)
No exact OS matches for host (test conditions non-ideal).
Network Distance: 23 hops
Service Info: OS: Windows; CPE: cpe:/o:microsoft:windows

Host script results:
|_smb2-security-mode: SMB: Couldn't find a NetBIOS name that works for the server. Sorry!
|_smb2-time: ERROR: Script execution failed (use -d to debug)

OS and Service detection performed. Please report any incorrect results at https://nmap.org/submit/ .
Nmap done: 1 IP address (1 host up) scanned in 845.56 seconds


 */

open class NetworkCtx(
    utils: KotlinScriptUtils
) : ScriptCtx(utils) {

    private val ctxDir = "/home/luis/.c0ckp1t/ctx/"
    private val propertiesFile = "ctx.properties"
    private val properties: Map<String, String>
    init {
        properties = readPropertiesFile("$ctxDir/$propertiesFile")
    }

    private val localUsername = properties["localUsername"]
    private val localPassword  = properties["localPassword"]


    val delugeStartPort = 6890
    val delugeEndPort = 6891
    private val localNodeId = 1L
    private val remoteNodeId = 2L


    // quick initial nmap scan
//    -sC: run default nmap scripts
//    -sV: detect service version
//    -O: detect OS
//    -oA: output all formats and store in file initial
//    nmap -sC -sV -O -oA initial 10.10.10.84
//    nmap -sC -sV -O 129.204.146.194 // don't do this from your ip!

    // full
//    nmap -sC -sV -p- -oA full 10.10.10.84

    // UDP scan
//    nmap -sU -p- -oA udp 10.10.10.84

    // TP-LINK
    // Model No. TL-WDR3500
    // http://192.168.0.1/
    // N600 Wireless Dual Band Router
    //  login= admin password= password
    // https://openwrt.org/toh/tp-link/tl-wdr3500

    // sb6190
    // Surf board Mac: 3C:04:61:3E:A0:7D
    // http://192.168.100.1/
    // admin / password.

    fun scanNetwork() {
        //
        // required root
        val cmd = "nmap -sP -PI -PT 192.168.0.1/24"
    }

    /*
    async nmap() {
    // all ports : sudo nmap -sS -sU -PN -p 1-65535 192.168.100.1
        this.runCommand( `nmap -T4 ${this.host}`)
    },
    async traceroute() {
        this.runCommand( `traceroute -q 1 -I ${this.host}`)
    },
    async ping() {
        this.runCommand(`ping -i 0.3 -c 3 ${this.host}`)
    },
    async ipAddr() {
        this.runCommand( `ip addr`)
    },
    async route() {
        this.runCommand(`ip route`)
    },
    async netstat() {
        this.runCommand(`netstat -an`)
    },

     */

    //________________________________________________________________________________
    // FIREWALL
    //________________________________________________________________________________
    val rootPrefix = "echo ${localPassword} |  sudo --prompt='' -S "

//    ufw logging on

    @C0ckp1tMethod("UFW Block IPs", "firewall")
    fun ufwBlockIPs()  : Flow<Any> = flow {
        val whiteIps= listOf(
                "85.228.179.247",
                "72.27.90.206",
                "202.80.218.154",
                "46.189.144.172",
                "100.37.138.226",
                "80.240.27.244",
                "72.252.216.168",
                "90.129.197.157",
                "194.207.95.187",
                "42.210.83.231",
                "103.255.136.126",
                "103.119.62.100",
                "80.6.113.138",
                "105.244.93.73",
                "86.107.104.90",
                "185.236.201.68",
                "146.255.182.235",
                "109.201.152.11",
                "120.29.73.162"

        )
        this forward sshFlow(remoteNodeId,"$rootPrefix ufw disable")
        this forward sshFlow(remoteNodeId,"$rootPrefix ufw --force reset")
        this forward sshFlow(remoteNodeId,"$rootPrefix ufw default deny incoming")
        this forward sshFlow(remoteNodeId,"$rootPrefix ufw default deny outgoing")
        this forward sshFlow(remoteNodeId,"$rootPrefix ufw allow 22/tcp")

        whiteIps.forEach {
            this forward sshFlow(remoteNodeId,"$rootPrefix ufw allow from $it")
        }


        this forward sshFlow(remoteNodeId,"$rootPrefix ufw --force enable ")
        this forward sshFlow(remoteNodeId,"$rootPrefix ufw status verbose")
    }


    @C0ckp1tMethod("UFW status", "firewall")
    fun ufwStatus()  : Flow<Any> = flow {
    this forward sshFlow(remoteNodeId,"$rootPrefix ufw status verbose")
//        this forward sshFlow(localNodeId,"echo \"luis\" | sudo -S id", 5000)
    }

    @C0ckp1tMethod("UFW Start", "firewall")
    fun ufwEnable()  : Flow<Any> = flow {
        this forward sshFlow(remoteNodeId,"$rootPrefix ufw --force enable ")
    }

    @C0ckp1tMethod("UFW Stop", "firewall")
    fun ufwDisable()  : Flow<Any> = flow {
        this forward sshFlow(remoteNodeId,"$rootPrefix ufw disable")
    }



    //________________________________________________________________________________
    // MAIN / REFRESH
    //________________________________________________________________________________
    override fun main(): Flow<Any> = flow {
        stdield("[main]")
    }

}
