package com.c0ckp1t.content


// C0CKP1T
import com.c0ckp1t.annotations.C0ckp1tMap
import com.c0ckp1t.annotations.C0ckp1tMethod
import com.c0ckp1t.annotations.RefreshResult
import com.c0ckp1t.services.ssh.BashService
import com.c0ckp1t.engine.ScriptCtx
import com.c0ckp1t.utils.WfResult
import com.c0ckp1t.engine.KotlinScriptUtils

// KOTLIN
import kotlin.reflect.KFunction
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.Job
import kotlinx.coroutines.*
import kotlin.random.Random
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll


// JSON
import io.vertx.kotlin.core.json.json
import io.vertx.kotlin.core.json.obj
import io.vertx.kotlin.core.json.array
import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject

// Maybe reference this in future:
// https://github.com/ansible/ansible/blob/d79b23910a1a16931885c5b3056179e72e0e6466/lib/ansible/module_utils/distro/_distro.py
open class RefreshCtx(
        utils: KotlinScriptUtils
) : ScriptCtx(utils) {

    // !!##START_WORKFLOW ---------------------------------------------------------------------------

    @C0ckp1tMap
    suspend fun os(): Map<String, String> {
        val stdout = utils.ssh(nodeId, "uname -snrmpio")
        val distro = utils.ssh(nodeId, "cat /etc/issue").split("\n").first()
//        val distro = sshFlowToString("cat /etc/os-release").split("\n").first()
        val columnsList = stdout.split(Regex("\\s")).filter { it != "" }
        return mapOf(
                "Kernel Name" to columnsList[0],
                "Node Name" to columnsList[1],
                "Kernel Release" to columnsList[2],
                "Machine" to columnsList[3],
                "Processor" to columnsList[4],
                "Platform" to columnsList[5],
                "Os" to columnsList[6],
                "Distribution" to distro
        )
    }
    @C0ckp1tMap
    suspend fun memory(): Map<String, String> {
        // TODO: doesn't work on alpine
        return try {
            val stdout = utils.ssh(nodeId, "free -h")
            val secondLine = stdout.split("\n")[1]
            val columnsList = secondLine.split(Regex("\\s")).filter { it != "" }
            mapOf(
                    "Total" to columnsList[1],
                    "Used" to columnsList[2],
                    "Available" to columnsList[6]
            )
        }catch(e: Exception) {
            mapOf()
        }
    }

    // ubuntu
    @C0ckp1tMap
    suspend fun cpu(): Map<String, String> {
        val cores = utils.ssh(nodeId, "cat /proc/cpuinfo | grep -m 1 'cpu cores'").split(":")
        val modelName = utils.ssh(nodeId, "cat /proc/cpuinfo | grep -m 1 'model name'").split(":")
        return mapOf(
                "Cores" to cores.last().trim(),
                "Model Name" to modelName.last().trim()
        )
    }

    @C0ckp1tMap
    suspend fun disk(): Map<String, String> {
        val stdout = utils.ssh(nodeId, "df -h")
        val stdoutLinesList = stdout.split("\n").drop(1)
        val rootRowColumns = stdoutLinesList.map {
            it.split(Regex("\\s")).filter { it != "" }
        }.filter { it.size == 6}.filter { it[5] == "/" }[0]
        return mapOf(
                "File System" to rootRowColumns[0],
                "Size" to rootRowColumns[1],
                "Used" to rootRowColumns[2],
                "Avail" to rootRowColumns[3],
                "Use Percent" to rootRowColumns[4],
                "Mount" to rootRowColumns[5]
        )
    }

    @C0ckp1tMap
    suspend fun network(): Map<String, String> {
        val hostname = utils.ssh(nodeId, "hostname")
        val ip_route = utils.ssh(nodeId, "ip route | grep -m 1 default")
        val ip_route_columns = ip_route.split(Regex("\\s"))
        val ip_addr = utils.ssh(nodeId,
                "ip addr | grep -A 5 ${ip_route_columns[4]}").split("\n")
        val ipv4 = ip_addr[2].split(Regex("\\s")).filter { it != "" }[1]
        val ipv6 = ip_addr[4].split(Regex("\\s")).filter { it != "" }[1]
        return mapOf(
                "Hostname" to hostname.trim(),
                "Route" to ip_route_columns[2].trim(),
                "Interface" to ip_route_columns[4].trim(),
                "IPV4" to ipv4,
                "IPV6" to ipv6
        )
    }

    //________________________________________________________________________________
    // MAIN / REFRESH
    //________________________________________________________________________________
    @C0ckp1tMethod("Main")
    override fun main(): Flow<Any> = flow {
        stdield("[main]")
    }

    // !!##END_WORKFLOW ---------------------------------------------------------------------------

}
