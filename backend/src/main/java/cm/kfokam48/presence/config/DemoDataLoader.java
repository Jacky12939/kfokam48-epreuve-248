package cm.kfokam48.presence.config;

import cm.kfokam48.presence.entity.Etudiant;
import cm.kfokam48.presence.entity.Promotion;
import cm.kfokam48.presence.repository.EtudiantRepository;
import cm.kfokam48.presence.repository.PromotionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DemoDataLoader implements CommandLineRunner {

    private final PromotionRepository promotionRepository;
    private final EtudiantRepository etudiantRepository;

    public DemoDataLoader(PromotionRepository promotionRepository,
                          EtudiantRepository etudiantRepository) {
        this.promotionRepository = promotionRepository;
        this.etudiantRepository = etudiantRepository;
    }

    @Override
    public void run(String... args) {
        // 1. Promotions
        if (promotionRepository.count() == 0) {
            promotionRepository.save(new Promotion("KF48-YA0"));
            promotionRepository.save(new Promotion("KF48-DLA"));
        }

        // 2. Étudiants (indépendant)
        if (etudiantRepository.count() == 0) {
            etudiantRepository.save(new Etudiant("Alice Ngono", 1L));
            etudiantRepository.save(new Etudiant("Bob Mbarga", 1L));
            etudiantRepository.save(new Etudiant("Chantal Fouda", 1L));
            etudiantRepository.save(new Etudiant("David Kamdem", 2L));
        }
    }
}
