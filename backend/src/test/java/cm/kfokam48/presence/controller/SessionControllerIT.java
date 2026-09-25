package cm.kfokam48.presence.controller;

import static org.assertj.core.api.Assertions.assertThat;

import cm.kfokam48.presence.entity.Promotion;
import cm.kfokam48.presence.repository.PromotionRepository;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class SessionControllerIT {

    @LocalServerPort int port;

    @Autowired private PromotionRepository promotionRepository;

    private final HttpClient http = HttpClient.newHttpClient();

    @BeforeEach
    void setUp() {
        promotionRepository.deleteAll();
        promotionRepository.save(new Promotion("TEST"));
    }

    private HttpResponse<String> post(String path, String body) throws Exception {
        HttpRequest req = HttpRequest.newBuilder()
            .uri(URI.create("http://localhost:" + port + path))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(body))
            .build();
        return http.send(req, HttpResponse.BodyHandlers.ofString());
    }

    @Test
    void post_session_nominal_retourne_201_et_code() throws Exception {
        HttpResponse<String> resp = post("/api/sessions",
            "{\"titre\":\"Cours test\",\"promotionId\":1}");

        assertThat(resp.statusCode()).isEqualTo(201);
        assertThat(resp.body()).contains("\"id\":");
        assertThat(resp.body()).contains("\"code\":\"");
        assertThat(resp.body()).contains("\"ouvertureAt\":");
        assertThat(resp.body()).contains("\"expirationAt\":");
    }

    @Test
    void post_session_sans_titre_retourne_400_format_impose() throws Exception {
        HttpResponse<String> resp = post("/api/sessions", "{\"promotionId\":1}");

        assertThat(resp.statusCode()).isEqualTo(400);
        assertThat(resp.body()).contains("\"code\":\"CHAMP_MANQUANT\"");
        assertThat(resp.body()).contains("\"message\":");
    }

    @Test
    void post_session_promotion_inconnue_retourne_404() throws Exception {
        HttpResponse<String> resp = post("/api/sessions",
            "{\"titre\":\"X\",\"promotionId\":9999}");

        assertThat(resp.statusCode()).isEqualTo(404);
        assertThat(resp.body()).contains("\"code\":\"PROMOTION_INCONNUE\"");
    }
}
