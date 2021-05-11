package co.uk.isxander.skyclient.installer.gui;

import co.uk.isxander.skyclient.installer.SkyClient;
import co.uk.isxander.skyclient.installer.repo.entry.ModEntry;
import co.uk.isxander.skyclient.installer.repo.entry.PackEntry;
import co.uk.isxander.skyclient.installer.utils.FileUtils;
import co.uk.isxander.skyclient.installer.utils.ImageUtils;
import co.uk.isxander.skyclient.installer.utils.UpdateHook;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;

public class MainGui {

    private final SkyClient sc;

    private final Map<ModEntry, JLabel> modIcons;
    private final Map<PackEntry, JLabel> packIcons;

    public MainGui(SkyClient sc) {
        this.sc = sc;
        this.modIcons = new HashMap<>();
        this.packIcons = new HashMap<>();

        sc.getRepositoryManager().fetchFiles(new UpdateHook() {
            @Override
            public void updateMod(ModEntry mod) {
                refreshModIcon(mod);
            }

            @Override
            public void updatePack(PackEntry pack) {
                refreshPackIcon(pack);
            }
        });

        Image icon = FileUtils.getResourceImage("skyclient.png");

        JFrame frame = new JFrame("SkyClient Installer (Java Edition)");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setIconImage(icon);
        frame.setResizable(false);

        Container container = frame.getContentPane();

        JPanel modPane = new JPanel();
        JPanel packPane = new JPanel();

        GridBagLayout gridBag = new GridBagLayout();
        GridBagConstraints constraints = new GridBagConstraints();

        container.setLayout(gridBag);
        modPane.setLayout(gridBag);
        packPane.setLayout(gridBag);
        constraints.fill = GridBagConstraints.HORIZONTAL;

        int i = 0;
        for (ModEntry mod : sc.getRepositoryManager().getModEntries()) {
            JLabel imgLabel = new JLabel(new ImageIcon(ImageUtils.getScaledImage(mod.getIconImage(), 50, 50)));
            imgLabel.setName(mod.getId());
            imgLabel.setPreferredSize(new Dimension(50, 50));
            constraints.insets = new Insets(1, 1, 1, 1);
            constraints.gridx = 0;
            constraints.gridy = i;
            gridBag.setConstraints(imgLabel, constraints);
            modIcons.put(mod, imgLabel);
            modPane.add(imgLabel);

            JCheckBox checkBox = new JCheckBox(mod.getDisplayName());
            checkBox.setName(mod.getId());
            checkBox.setSelected(mod.isEnabled());
            checkBox.addActionListener((action) -> mod.setEnabled(checkBox.isSelected()));
            constraints.gridx = 1;
            constraints.gridy = i;
            gridBag.setConstraints(checkBox, constraints);
            modPane.add(checkBox);

            JButton actionButton = new JButton("^");
            actionButton.setName(mod.getId());
            JPopupMenu menu = genPopup(mod);
            actionButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    menu.show(e.getComponent(), e.getX(), e.getY());
                }
            });
            constraints.gridx = 2;
            constraints.gridy = i;
            gridBag.setConstraints(actionButton, constraints);
            modPane.add(actionButton);

            i++;
        }

        frame.pack();
        frame.setVisible(true);
    }

    private static JPopupMenu genPopup(ModEntry mod) {

    }

    public void refreshModIcon(ModEntry mod) {
        modIcons.get(mod).setIcon(new ImageIcon(ImageUtils.getScaledImage(mod.getIconImage(), 50, 50)));
    }

    public void refreshPackIcon(PackEntry pack) {
        packIcons.get(pack).setIcon(new ImageIcon(ImageUtils.getScaledImage(pack.getIconImage(), 50, 50)));
    }

}
