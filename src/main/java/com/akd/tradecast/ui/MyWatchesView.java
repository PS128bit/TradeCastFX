package com.akd.tradecast.ui;

import com.akd.tradecast.data.MockData;
import com.akd.tradecast.model.Quote;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Screen 2 container: the My Watches window with its row of watch tabs
 * (1st / 2nd / 3rd Profile, Sector Watch ... Portfolio Watch).
 * A timeline simulates the live market feed by nudging prices every 1.5 seconds.
 */
public class MyWatchesView extends BorderPane {

    public static final String PORTFOLIO_WATCH = "Portfolio Watch";
    private static final String[] WATCH_TABS = {"1st Profile", "2nd Profile", "3rd Profile", "Sector Watch",
            "Index Watch", "Spot Watch", "Future Watch", "Cap Watch"};

    private final TabPane tabs = new TabPane();
    private final List<WatchProfileView> profiles = new ArrayList<>();
    private final Map<String, Quote> universe = MockData.universe();
    private final Random random = new Random(7);

    public MyWatchesView() {
        tabs.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        for (String name : WATCH_TABS) {
            WatchProfileView view = new WatchProfileView(name, MockData.watchScrips(name), universe);
            profiles.add(view);
            tabs.getTabs().add(new Tab(name, view));
        }
        tabs.getTabs().add(new Tab(PORTFOLIO_WATCH, new PortfolioWatchView()));
        setCenter(tabs);
        startFeed();
    }

    public void selectTab(String name) {
        for (Tab t : tabs.getTabs()) {
            if (t.getText().equals(name)) tabs.getSelectionModel().select(t);
        }
    }

    /** Fake market feed: a few random scrips tick each cycle and their volume cells flash. */
    private void startFeed() {
        List<Quote> all = new ArrayList<>(universe.values());
        Timeline feed = new Timeline(new KeyFrame(Duration.millis(1500), e -> {
            for (Quote q : all) q.setFlash(false, false);
            for (int i = 0; i < 4; i++) {
                Quote q = all.get(random.nextInt(all.size()));
                double step = q.getLastPrice() > 100 ? 0.25 : 0.05;
                q.tick((random.nextInt(3) - 1) * step, 50L + random.nextInt(5000));
                boolean buySide = random.nextBoolean();
                if (buySide) {
                    q.setVolumes(q.getBuyVolume() + 100 + random.nextInt(2000), q.getSellVolume());
                } else {
                    q.setVolumes(q.getBuyVolume(), q.getSellVolume() + 100 + random.nextInt(2000));
                }
                q.setFlash(buySide, !buySide);
            }
            profiles.forEach(WatchProfileView::refresh);
        }));
        feed.setCycleCount(Timeline.INDEFINITE);
        feed.play();
    }
}
