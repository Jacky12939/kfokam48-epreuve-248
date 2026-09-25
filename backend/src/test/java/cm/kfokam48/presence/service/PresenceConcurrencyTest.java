package cm.kfokam48.presence.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import cm.kfokam48.presence.dto.PresenceRequest;
import cm.kfokam48.presence.entity.Promotion;
import cm.kfokam48.presence.entity.Session;
import cm.kfokam48.presence.exception.DejaPresentException;
import cm.kfokam48.presence.repository.PresenceRepository;
import cm.kfokam48.presence.repository.PromotionRepository;
import cm.kfokam48.presence.repository.SessionRepository;
import java.time.LocalDateTime;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * Reproduit le bug signalé dans l'enveloppe :
 * deux étudiants qui tapent le code en même temps → un seul enregistré, l'autre renvoie 500.
 *
 * Le test simule la concurrence en lançant N threads qui appellent marquer() avec le même code
 * et le même etudiantId. Résultat attendu après correction :
 *  - 1 seul succès
 *  - N-1 DejaPresentException
 *  - 0 autre type d'exception (pas de DataIntegrityViolationException)
 */
@SpringBootTest
@ActiveProfiles("test")
class PresenceConcurrencyTest {

    @Autowired private PresenceService presenceService;
    @Autowired private SessionRepository sessionRepository;
    @Autowired private PromotionRepository promotionRepository;
    @Autowired private PresenceRepository presenceRepository;

    private String code;

    @BeforeEach
    void setUp() {
        presenceRepository.deleteAll();
        sessionRepository.deleteAll();
        promotionRepository.deleteAll();

        Promotion promo = promotionRepository.save(new Promotion("TEST"));
        Session s = new Session();
        s.setTitre("Test concurrency");
        s.setCode("999999");
        s.setPromotionId(promo.getId());
        s.setOuvertureAt(LocalDateTime.now());
        s.setExpirationAt(LocalDateTime.now().plusMinutes(15));
        sessionRepository.save(s);
        code = s.getCode();
    }

    @Test
    void deux_appels_simultanes_avec_meme_etudiant_doivent_donner_un_seul_succes() throws Exception {
        int threads = 4;
        ExecutorService pool = Executors.newFixedThreadPool(threads);
        CountDownLatch start = new CountDownLatch(1);
        CountDownLatch done = new CountDownLatch(threads);

        AtomicInteger succes = new AtomicInteger();
        AtomicInteger dejaPresent = new AtomicInteger();
        AtomicInteger autres = new AtomicInteger();

        for (int i = 0; i < threads; i++) {
            pool.submit(() -> {
                try {
                    start.await();
                    presenceService.marquer(new PresenceRequest(code, null, 1L, null));
                    succes.incrementAndGet();
                } catch (DejaPresentException e) {
                    dejaPresent.incrementAndGet();
                } catch (Exception e) {
                    autres.incrementAndGet();
                    System.err.println("Exception inattendue : " + e.getClass().getName() + " — " + e.getMessage());
                } finally {
                    done.countDown();
                }
            });
        }

        start.countDown();
        done.await(10, TimeUnit.SECONDS);
        pool.shutdown();

        System.out.println("Résultat : succès=" + succes.get()
            + ", dejaPresent=" + dejaPresent.get()
            + ", autres=" + autres.get());

        assertThat(succes.get()).isEqualTo(1);
        assertThat(dejaPresent.get()).isEqualTo(threads - 1);
        assertThat(autres.get()).isZero();
    }
}
