package cm.kfokam48.presence.config;

import cm.kfokam48.presence.entity.Promotion;
import cm.kfokam48.presence.repository.PromotionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DemoDataLoader implements CommandLineRunner {

    private final PromotionRepository promotionRepository;

    public DemoDataLoader(PromotionRepository promotionRepository) {
        this.promotionRepository = promotionRepository;
    }

    @Override
    public void run(String... args) {
        if (promotionRepository.count() == 0) {
            promotionRepository.save(new Promotion("KF48-YA0"));
            promotionRepository.save(new Promotion("KF48-DLA"));
        }
    }
}
