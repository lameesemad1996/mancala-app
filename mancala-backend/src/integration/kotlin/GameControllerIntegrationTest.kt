import com.example.mancala.MancalaApplication
import com.example.mancala.entity.GameState
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.validation.annotation.Validated

@SpringBootTest(classes = [MancalaApplication::class])
@AutoConfigureMockMvc
@Validated
class GameControllerIntegrationTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    private lateinit var createdGameState: GameState

    @BeforeEach
    fun setup() {
        val result = mockMvc.perform(post("/game/create"))
            .andExpect(status().isOk)
            .andReturn()
        createdGameState = ObjectMapper().readValue(result.response.contentAsString, GameState::class.java)
    }

    val moveEndpoint = "/game/move"
    val resetEndpoint = "/game/reset"
    val stateEndpoint = "/game/state"
    val errorJSONPath = "$.message"

    @Test
    fun getGameStateReturnsCurrentGameState() {
        mockMvc.perform(get("$stateEndpoint/${createdGameState.id}"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.board.pits").exists())
            .andExpect(jsonPath("$.currentPlayer").exists())
    }

    @Test
    fun postMoveUpdatesGameState() {
        mockMvc.perform(post("$moveEndpoint/${createdGameState.id}").param("pitIndex", "0"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.board.pits[0]").value(0)) // Assuming pit 0 has been played
    }

    @Test
    fun postResetReturnsInitialGameState() {
        mockMvc.perform(post("$resetEndpoint/${createdGameState.id}"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.board.pits[0]").value(6)) // Assuming pits are reset to 6 stones each
    }

    /**
     * Edge Case: Attempt to make a move with an invalid (negative) pit index.
     */
    @Test
    fun postMoveWithNegativePitIndexReturnsBadRequest() {
        mockMvc.perform(post("$moveEndpoint/${createdGameState.id}").param("pitIndex", "-1"))
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath(errorJSONPath).value("pitIndex: Invalid pit index"))
    }

    /**
     * Edge Case: Attempt to make a move with a pit index out of bounds.
     * Assuming the game has 14 pits (0-13), so index 14 is invalid.
     */
    @Test
    fun postMoveWithOutOfBoundsPitIndexReturnsBadRequest() {
        mockMvc.perform(post("$moveEndpoint/${createdGameState.id}").param("pitIndex", "14"))
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath(errorJSONPath).value("pitIndex: Invalid pit index"))
    }

    /**
     * Edge Case: Attempt to make a move on an empty pit.
     * First, play pit 0 to empty it, then attempt to play it again.
     */
    @Test
    fun postMoveOnEmptyPitReturnsBadRequest() {
        // First move to empty pit 0
        mockMvc.perform(post("$moveEndpoint/${createdGameState.id}").param("pitIndex", "0"))
            .andExpect(status().isOk)

        // Attempt to move again on the now-empty pit 0
        mockMvc.perform(post("$moveEndpoint/${createdGameState.id}").param("pitIndex", "0"))
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath(errorJSONPath).value("Invalid move. You cannot select an empty pit."))
    }

    /**
     * Edge Case: High-volume requests - making multiple moves in quick succession.
     * Verify the system's stability under rapid sequential requests.
     */
    @Test
    fun postMultipleMovesInQuickSuccession() {
        val pitIndices = listOf("1", "8", "3", "9", "2", "10")  // Alternating moves between players

        for (pitIndex in pitIndices) {
            mockMvc.perform(post("$moveEndpoint/${createdGameState.id}").param("pitIndex", pitIndex))
                .andExpect(status().isOk)
                .andReturn()
        }
    }

    /**
     * Edge Case: Attempt to make a move with invalid (non-integer) pit index.
     */
    @Test
    fun postMoveWithNonIntegerPitIndexReturnsBadRequest() {
        mockMvc.perform(post("$moveEndpoint/${createdGameState.id}").param("pitIndex", "invalid"))
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath(errorJSONPath).value("Pit index must be an integer"))
    }


    /**
     * Edge Case: Attempt to access an undefined endpoint.
     */
    @Test
    fun accessUndefinedEndpointReturnsNotFound() {
        mockMvc.perform(get("/game/undefinedEndpoint"))
            .andExpect(status().isNotFound)
    }

    /**
     * Edge Case: Attempt to send a request with missing required parameters.
     */
    @Test
    fun postMoveWithMissingParameterReturnsBadRequest() {
        mockMvc.perform(
            post("$moveEndpoint/${createdGameState.id}")
                .contentType(MediaType.APPLICATION_JSON)
                .content("")
        )
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath(errorJSONPath).value("Pit index is required"))
    }
}
