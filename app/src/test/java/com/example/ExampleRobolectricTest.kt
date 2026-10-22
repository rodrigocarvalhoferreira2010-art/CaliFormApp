package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.caliform.data.CaliformRepository
import com.example.caliform.data.CalisthenicsData
import com.example.caliform.model.BodyArea
import com.example.caliform.model.DifficultyLevel
import com.example.caliform.model.DurationCategory
import com.example.caliform.model.Equipment
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

    @Test
    fun `read string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("CaliForm", appName)
    }

    @Test
    fun `repository filters exercises by body area and difficulty`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val repo = CaliformRepository(context)

        // Upper body beginner
        val upperBeginner = repo.getExercises(
            bodyArea = BodyArea.UPPER_BODY,
            difficulty = DifficultyLevel.BEGINNER,
            equipment = null,
            searchQuery = ""
        )
        assertTrue(upperBeginner.isNotEmpty())
        assertTrue(upperBeginner.all { it.bodyArea == BodyArea.UPPER_BODY && it.difficulty == DifficultyLevel.BEGINNER })

        // Core advanced
        val coreAdvanced = repo.getExercises(
            bodyArea = BodyArea.CORE,
            difficulty = DifficultyLevel.ADVANCED,
            equipment = null,
            searchQuery = ""
        )
        assertTrue(coreAdvanced.isNotEmpty())
        assertTrue(coreAdvanced.all { it.bodyArea == BodyArea.CORE && it.difficulty == DifficultyLevel.ADVANCED })
    }

    @Test
    fun `repository filters exercises by equipment`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val repo = CaliformRepository(context)

        val bar175Ex = repo.getExercises(null, null, Equipment.BARRA_175, "")
        assertTrue(bar175Ex.isNotEmpty())
        assertTrue(bar175Ex.all { it.equipmentNeeded.contains(Equipment.BARRA_175) })

        val dipsEx = repo.getExercises(null, null, Equipment.PARALELAS_1M, "")
        assertTrue(dipsEx.isNotEmpty())
        assertTrue(dipsEx.all { it.equipmentNeeded.contains(Equipment.PARALELAS_1M) })
    }

    @Test
    fun `AI generator creates customized workout for selected equipments`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val repo = CaliformRepository(context)

        val aiWorkout = repo.generateAiWorkout(
            difficulty = DifficultyLevel.BEGINNER,
            durationMinutes = 15,
            selectedEquipment = listOf(Equipment.BARRA_175, Equipment.BANCO_ABDOMINAIS)
        )

        assertNotNull(aiWorkout)
        assertEquals(DifficultyLevel.BEGINNER, aiWorkout.difficulty)
        assertEquals(15, aiWorkout.durationMinutes)
        assertTrue(aiWorkout.isAiGenerated)
        assertTrue(aiWorkout.exerciseIds.isNotEmpty())
    }

    @Test
    fun `repository records completed workout into history`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val repo = CaliformRepository(context)

        val initialHistoryCount = repo.history.value.size
        val initialWorkoutsCompleted = repo.currentUser.value.totalWorkoutsCompleted

        repo.recordCompletedWorkout(
            workoutTitle = "Treino 15 min: Circuito do Parque",
            durationMinutes = 15,
            exerciseNames = listOf("Flexões", "Remada Australiana"),
            difficulty = DifficultyLevel.BEGINNER,
            ratingStars = 5,
            notes = "Boa sessão de treino."
        )

        assertEquals(initialHistoryCount + 1, repo.history.value.size)
        assertEquals(initialWorkoutsCompleted + 1, repo.currentUser.value.totalWorkoutsCompleted)
        assertEquals("Treino 15 min: Circuito do Parque", repo.history.value.first().workoutTitle)
    }

    @Test
    fun `repository handles login and profile switching`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val repo = CaliformRepository(context)

        repo.loginOrCreateUser(
            name = "Rui Santos",
            email = "rui.santos@aluno.pt",
            studentLevel = "Nível 1 • Aluno Iniciante",
            targetGoal = "Aprender flexões e barra baixa"
        )

        assertEquals("Rui Santos", repo.currentUser.value.name)
        assertEquals("rui.santos@aluno.pt", repo.currentUser.value.email)
    }

    @Test
    fun `student leaderboard calculates rank and sorted order`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val repo = CaliformRepository(context)

        val leaderboard = repo.getStudentLeaderboard()
        assertTrue(leaderboard.isNotEmpty())

        // Verify strictly descending order by points
        for (i in 0 until leaderboard.size - 1) {
            assertTrue(leaderboard[i].points >= leaderboard[i + 1].points)
            assertEquals(i + 1, leaderboard[i].rank)
        }

        // Verify current user identification
        val currentUserRank = leaderboard.find { it.isCurrentUser }
        assertNotNull(currentUserRank)
        assertEquals(repo.currentUser.value.name, currentUserRank!!.name)
    }

    @Test
    fun `AI chat memory generates greeting with mission and handles acceptance`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val repo = CaliformRepository(context)

        repo.initAiChatMemory()
        val messages = repo.chatMessages.value
        assertTrue(messages.isNotEmpty())

        val introMsg = messages.first()
        assertTrue(introMsg.isFromAi)
        assertNotNull(introMsg.recommendedWorkout)

        // Accept the mission
        val acceptedWorkout = repo.acceptMission(introMsg.id)
        assertNotNull(acceptedWorkout)
        assertEquals(introMsg.recommendedWorkout!!.id, acceptedWorkout!!.id)

        // Verify status is updated
        val updatedMsg = repo.chatMessages.value.find { it.id == introMsg.id }
        assertEquals(com.example.caliform.model.MissionStatus.ACCEPTED, updatedMsg!!.missionStatus)
    }

    @Test
    fun `authentication flow supports login and logout`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val repo = CaliformRepository(context)

        // Login with email
        val loginSuccess = repo.loginWithEmailPassword("aluno.teste@caliform.pt", "123456")
        assertTrue(loginSuccess)
        assertTrue(repo.isLoggedIn.value)

        // Logout
        repo.logout()
        assertTrue(!repo.isLoggedIn.value)

        // Login with OAuth
        val oauthSuccess = repo.loginWithOAuth("Google", null)
        assertTrue(oauthSuccess)
        assertTrue(repo.isLoggedIn.value)
    }
}
