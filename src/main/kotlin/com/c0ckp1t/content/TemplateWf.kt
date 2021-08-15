package com.c0ckp1t.content

import com.c0ckp1t.services.ssh.BashService
import com.c0ckp1t.engine.ScriptCtx
import com.c0ckp1t.utils.WfResult
import com.c0ckp1t.engine.KotlinScriptUtils
import com.c0ckp1t.annotations.C0ckp1tAnnotations
import com.c0ckp1t.annotations.C0ckp1tMap
import com.c0ckp1t.annotations.C0ckp1tMethod
import com.c0ckp1t.annotations.C0ckp1tCheck
import com.c0ckp1t.utils.*

import com.c0ckp1t.utils.createDir
import com.c0ckp1t.utils.readPropertiesFile
import com.c0ckp1t.utils.writePropertiesFile
import com.c0ckp1t.utils.writeTextFile
import com.c0ckp1t.engine.CheckObj

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

// HTTP
import io.vertx.kotlin.ext.web.client.sendAwait
import io.vertx.ext.web.client.WebClient
import io.vertx.core.Vertx

// JSON
import io.vertx.kotlin.core.json.json
import io.vertx.kotlin.core.json.obj
import io.vertx.kotlin.core.json.array
import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject
//import org.fissore.slf4j.FluentLoggerFactory

/*

 */

open class TemplateWf (
    utils: KotlinScriptUtils
) : ScriptCtx(utils) {

    // !!##START_WORKFLOW ---------------------------------------------------------------------------

    private val c0ckp1tDir = "/opt/c0ckp1t"
    private val propertiesFile = "ctx.properties"
    private val properties: Map<String, String> = readPropertiesFile("$c0ckp1tDir/$propertiesFile")

    private val localUsername = properties["localUsername"]
    private val localPassword  = properties["localPassword"]

    private val localNodeId = 1L
    private val remoteNodeId = 2L

    val rootPrefix = "echo ${localPassword} |  sudo --prompt='' -S "

    //________________________________________________________________________________
    // METHODS
    //________________________________________________________________________________

    @C0ckp1tMethod("UFW status", "firewall")
    fun ufwStatus()  : Flow<Any> = flow {
        this forward sshFlow(remoteNodeId,"$rootPrefix ufw status verbose")
    }

    //________________________________________________________________________________
    // MAIN / REFRESH
    //________________________________________________________________________________
    override fun main(): Flow<Any> = flow {
        stdield("[main]")
    }

    // !!##END_WORKFLOW ---------------------------------------------------------------------------

}
