package com.c0ckp1t.content


import com.c0ckp1t.annotations.C0ckp1tAnnotations
import com.c0ckp1t.engine.KotlinScriptUtils
import com.c0ckp1t.engine.ScriptCtx
import com.c0ckp1t.services.ssh.BashService
import com.c0ckp1t.services.ssh.NodeConfig
import com.c0ckp1t.utils.WfResult
import com.c0ckp1t.utils.readPropertiesFile
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.io.File

/**
 * Unit Test - NetworkCtxUnitTest
 *
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class NetworkCtxUnitTest {


    private val bash = BashService()
    private val utils = KotlinScriptUtils(bash)
    private val writePath = "src/main/resources/networkCtx.html"

    private val ctxDir = "/home/luis/.c0ckp1t/ctx/"
    private val propertiesFile = "ctx.properties"

    @BeforeAll
    fun beforeAll() {
        runBlocking {
            val properties = readPropertiesFile("$ctxDir/$propertiesFile")
            utils.addSshNode(2, NodeConfig(
                    username = properties["localUsername"] ?: "",
                    hostname = "localhost",
                    port = 22,
                    password = properties["localPassword"] ?: ""
            ))
            utils.addSession(2)
        }
    }


    // ________________________________________________________________________________
    // TEST CASES
    // ________________________________________________________________________________
    @Test
    fun `refresh`() {
        val ctx = NetworkCtx(utils)
        runBlocking {
            val results = C0ckp1tAnnotations.process(ctx, 0)
//            GUIAnnotation.writeIndexHtml(writePath, results)
            println(results)
        }
    }

    // ________________________________________________________________________________
    // FIREWALL
    // ________________________________________________________________________________
    @Test
fun `enable and disable ufw`() {
        val ctx = NetworkCtx(utils)
        runBlocking {
            ctx.ufwStatus().collect { println(it) }
//            ctx.ufwEnable().collect { println(it) }
            ctx.ufwDisable().collect { println(it) }
            ctx.ufwStatus().collect { println(it) }
        }
    }

    @Test
    fun `block ip list`() {
        val ctx = NetworkCtx(utils)
        runBlocking {
            ctx.ufwBlockIPs().collect { println(it) }
        }
    }

}

