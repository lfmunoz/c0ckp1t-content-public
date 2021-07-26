package com.c0ckp1t.content

import com.c0ckp1t.annotations.C0ckp1tAnnotations
import com.c0ckp1t.engine.KotlinScriptUtils
import com.c0ckp1t.services.ssh.BashService
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

/**
 * Unit Test -
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RefreshCtxUnitTest {

    private val bash = BashService()
    private val utils = KotlinScriptUtils(bash)

    //________________________________________________________________________________
    // Test Cases
    //________________________________________________________________________________
    @Test
    fun `refresh`() {
        val ctx = RefreshCtx(utils)
        runBlocking {
            val results = C0ckp1tAnnotations.process(ctx, 0)
            println(results)
        }
    }

    @Test
    fun `cores`() {
        val ctx = RefreshCtx(utils)
        runBlocking {
            println(ctx.cpu())
        }
    }

    @Test
    fun `disk`() {
        val ctx = RefreshCtx(utils)
        runBlocking {
            println(ctx.disk())
        }
    }
    // ________________________________________________________________________________
    // Helper Methods
    // ________________________________________________________________________________
//    private fun buildWsPacket(wfDto: WfDto) : WsPacket {
//    }


}
