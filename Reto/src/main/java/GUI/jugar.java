/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import CLASES.jugador;
import CLASES.participa;
import CLASES.preguntas;
import CLASES.rondas;
import CONTROLADORES.ctrlquestions;
import PERSISTENCIA.CPrincipal;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import javax.swing.JRadioButton;

/**
 *
 * @author franc
 */
public class jugar extends javax.swing.JFrame {

    /**
     * Creates new form jugar
     */
    public static rondas ronda = null;
    public static participa participacion = null;
    public static preguntas preg = null;
    public static jugador playeractual = new jugador();
    public static List<participa> participacionesdeljugador = new ArrayList<participa>();
    ctrlquestions CQ = new ctrlquestions();
    int puntos = 0;
    boolean siguientepressed = false;

    public jugar(jugador player) {
        initComponents();
        puntos = 0;
        ronda = new rondas();
        ronda.setNumero_rondas(5);
        ronda.setRonda_actual(1);

        participacion = new participa();
        preg = new preguntas();
        preg = CQ.pregunta1();
        playeractual = player;
        rellenarcampos(preg);

    }

    public void chekearrespuesta(JRadioButton radiobtm) {
        boolean respuestacorrecta = false;
        respuesta1.setEnabled(false);
        respuesta2.setEnabled(false);
        respuesta3.setEnabled(false);
        respuesta4.setEnabled(false);
        CPrincipal.getInstance().persist(ronda);
        CPrincipal.getInstance().persist(participacion);

        ronda.setPartipacion(participacion);
        if (radiobtm.getText().equals(preg.getRespuestac())) {
            respuestacorrecta = true;
            if (ronda.getRonda_actual() == 1) {
                participacion.setPremioronda(100);
                acumularpuntos(100);
            } else if (ronda.getRonda_actual() == 2) {
                participacion.setPremioronda(200);
                acumularpuntos(200);
            } else if (ronda.getRonda_actual() == 3) {
                participacion.setPremioronda(300);
                acumularpuntos(300);
            } else if (ronda.getRonda_actual() == 4) {
                participacion.setPremioronda(400);
                acumularpuntos(400);
            } else if (ronda.getRonda_actual() == 5) {
                participacion.setPremioronda(500);
                acumularpuntos(500);
            }
            jLabelRespuesta.setText("Correcta");
            participacion.setEstadoronda("Gano");
        } else {
            respuestacorrecta = false;
            jLabelRespuesta.setText("Incorrecta, la respuesta correcta era: " + preg.getRespuestac());
            participacion.setEstadoronda("Perdio");
            bloqueartodo();
            participacion.setPreguntaronda(preg.toString());
            participacion.setRespuestaEelegida(radiobtm.getText());
            participacion.setRespuestaC(preg.getRespuestac());
            participacion.setRondajugada(ronda);
            participacion.setParticipante(playeractual);
            int rondasjugadas = playeractual.getRondasjugadas();
            playeractual.setRondasjugadas(rondasjugadas + 1);
            if (playeractual.getParticipaciones().isEmpty()) {
                playeractual.setParticipaciones(participacionesdeljugador);
            } else {
                participacionesdeljugador = playeractual.getParticipaciones();
                participacionesdeljugador.add(participacion);
                playeractual.setParticipaciones(participacionesdeljugador);

            }
            findeljuego();
        }
        if (respuestacorrecta) {

            int rondasjugadas = playeractual.getRondasjugadas();
            playeractual.setRondasjugadas(rondasjugadas + 1);
            //  playeractual.setPuntos(puntos + participacion.getPremioronda());
            if (playeractual.getParticipaciones().isEmpty()) {
                playeractual.setParticipaciones(participacionesdeljugador);
            } else {
                participacionesdeljugador = playeractual.getParticipaciones();
                participacionesdeljugador.add(participacion);
                playeractual.setParticipaciones(participacionesdeljugador);

            }

            CPrincipal.getInstance().merge(ronda);
            CPrincipal.getInstance().merge(participacion);
            CPrincipal.getInstance().merge(playeractual);
            if (ronda.getRonda_actual() == 5) {
                //end game

            }
        }
    }

    public void findeljuego() {
        int puntosactuales;
        puntosactuales = playeractual.getPuntos();
        playeractual.setPuntos(puntos + puntosactuales);
        CPrincipal.getInstance().merge(playeractual);
        //  CPrincipal.getInstance().refresh(playeractual);
        menuprincipal menuprincipal = new menuprincipal(playeractual);
        menuprincipal.setVisible(true);
        this.dispose();
    }

    public void bloqueartodo() {
        siguientepressed = true;
        nextround.setEnabled(false);

        respuesta1.setEnabled(false);
        respuesta2.setEnabled(false);
        respuesta3.setEnabled(false);
        respuesta4.setEnabled(false);
        respuesta1.setSelected(false);
        respuesta2.setSelected(false);
        respuesta3.setSelected(false);
        respuesta4.setSelected(false);
        siguientepressed = false;

    }

    private jugar() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void acumularpuntos(int premio) {
        puntos = puntos + premio;
        String puntosstr;
        puntosstr = String.valueOf(puntos);
        acumuladotxt.setText(puntosstr);

    }

    private List desordenadossinrepetir(String[] respuestas) {
        Random random = new Random();
        String[] respuestasdesordenadas = new String[4];
        int i = 0;
        while (i < 4) {
            int pos = random.nextInt(respuestas.length);
            if (!Arrays.asList(respuestasdesordenadas).contains(respuestas[pos])) {
                respuestasdesordenadas[i] = respuestas[pos];
                i++;
            }
        }
        return Arrays.asList(respuestasdesordenadas);
    }

    public void rellenarcampos(preguntas preg1) {
        jLabel1.setText(preg.getPregunta());
        int i = 1;
        String respuesta1x = preg1.getRespuestac();
        String respuesta2x = preg1.getRespuesta1();
        String respuesta3x = preg1.getRespuesta2();
        String respuesta4x = preg1.getRespuesta3();
        String[] x = {respuesta1x, respuesta2x, respuesta3x, respuesta4x};
        List opciones = desordenadossinrepetir(x);
        for (i = 0; i < 4; i = i + 1) {
            switch (i) {
                case 0:
                    respuesta1.setText(opciones.get(i).toString());

                case 1:
                    respuesta2.setText(opciones.get(i).toString());

                case 2:
                    respuesta3.setText(opciones.get(i).toString());

                case 3:
                    respuesta4.setText(opciones.get(i).toString());

                default:

            }

        }

    }

    public void activarbotones() {

        nextround.setEnabled(false);

        respuesta1.setEnabled(true);
        respuesta2.setEnabled(true);
        respuesta3.setEnabled(true);
        respuesta4.setEnabled(true);
        respuesta1.setSelected(false);
        respuesta2.setSelected(false);
        respuesta3.setSelected(false);
        respuesta4.setSelected(false);
        siguientepressed = false;

    }

    public void pasarsiguienteronda() {
        if (ronda.getRonda_actual() < 5) {
            activarbotones();
            jLabelRespuesta.setText("");
            int num = ronda.getRonda_actual();

            rondas rondanueva = new rondas();
            ronda = rondanueva;
            participacion = new participa();
            ronda.setNumero_rondas(5);

            participacion = new participa();
            preg = new preguntas();
            preg = CQ.pregunta1();
            switch (num) {
                case 1:
                    ronda.setRonda_actual(2);
                    preg = CQ.pregunta2();
                    jLabel6.setText("2");
                    rellenarcampos(preg);
                    break;
                case 2:
                    ronda.setRonda_actual(3);
                    jLabel6.setText("3");
                    preg = CQ.pregunta3();
                    rellenarcampos(preg);
                    break;
                case 3:
                    ronda.setRonda_actual(4);
                    jLabel6.setText("4");
                    preg = CQ.pregunta4();
                    rellenarcampos(preg);
                    break;

                case 4:
                    ronda.setRonda_actual(5);
                    jLabel6.setText("5");
                    preg = CQ.pregunta5();
                    rellenarcampos(preg);
                    break;
                default:
                    break;
            }

        } else {
            findeljuego();
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        respuesta1 = new javax.swing.JRadioButton();
        respuesta2 = new javax.swing.JRadioButton();
        respuesta3 = new javax.swing.JRadioButton();
        respuesta4 = new javax.swing.JRadioButton();
        labelrespuesta = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabelRespuesta = new javax.swing.JLabel();
        nextround = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        acumuladotxt = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel1.setText("jLabel1");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 140, -1, -1));

        respuesta1.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        respuesta1.setText("jRadioButton1");
        respuesta1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                respuesta1ItemStateChanged(evt);
            }
        });
        respuesta1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                respuesta1StateChanged(evt);
            }
        });
        getContentPane().add(respuesta1, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 180, -1, -1));

        respuesta2.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        respuesta2.setText("jRadioButton2");
        respuesta2.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                respuesta2ItemStateChanged(evt);
            }
        });
        getContentPane().add(respuesta2, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 210, -1, -1));

        respuesta3.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        respuesta3.setText("jRadioButton3");
        respuesta3.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                respuesta3ItemStateChanged(evt);
            }
        });
        getContentPane().add(respuesta3, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 250, -1, -1));

        respuesta4.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        respuesta4.setText("jRadioButton4");
        respuesta4.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                respuesta4ItemStateChanged(evt);
            }
        });
        respuesta4.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                respuesta4StateChanged(evt);
            }
        });
        getContentPane().add(respuesta4, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 290, -1, -1));

        labelrespuesta.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        labelrespuesta.setText("Respuesta:");
        getContentPane().add(labelrespuesta, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 340, -1, -1));

        jLabel3.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel3.setText("Premio Ronda:");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(362, 54, -1, -1));

        jLabel4.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel4.setText("Acumulado:");
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(37, 54, -1, -1));

        jLabel5.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        jLabel5.setText("Ronda Acual");
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(153, 6, -1, -1));

        jLabel6.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        jLabel6.setText("1");
        getContentPane().add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(281, 6, -1, -1));

        jLabel7.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        jLabel7.setText("/5");
        getContentPane().add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(297, 6, -1, -1));

        jLabelRespuesta.setText(" ");
        getContentPane().add(jLabelRespuesta, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 340, 302, -1));

        nextround.setText("Siguiente Ronda");
        nextround.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nextroundActionPerformed(evt);
            }
        });
        getContentPane().add(nextround, new org.netbeans.lib.awtextra.AbsoluteConstraints(513, 391, -1, -1));

        jButton1.setText("Terminar juego");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(45, 391, -1, -1));

        acumuladotxt.setText("0");
        getContentPane().add(acumuladotxt, new org.netbeans.lib.awtextra.AbsoluteConstraints(124, 56, -1, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void respuesta4StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_respuesta4StateChanged

        // TODO add your handling code here:
    }//GEN-LAST:event_respuesta4StateChanged

    private void respuesta1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_respuesta1StateChanged

        // TODO add your handling code here:
    }//GEN-LAST:event_respuesta1StateChanged

    private void respuesta1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_respuesta1ItemStateChanged
        if (!siguientepressed) { //si es false
            chekearrespuesta(respuesta1);
            nextround.setEnabled(true);
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_respuesta1ItemStateChanged

    private void nextroundActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nextroundActionPerformed
        siguientepressed = true;
        pasarsiguienteronda();
    }//GEN-LAST:event_nextroundActionPerformed

    private void respuesta2ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_respuesta2ItemStateChanged
        if (!siguientepressed) {
            chekearrespuesta(respuesta2);
            nextround.setEnabled(true);
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_respuesta2ItemStateChanged

    private void respuesta3ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_respuesta3ItemStateChanged
        if (!siguientepressed) {
            chekearrespuesta(respuesta3);
            nextround.setEnabled(true);
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_respuesta3ItemStateChanged

    private void respuesta4ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_respuesta4ItemStateChanged
        if (!siguientepressed) {
            chekearrespuesta(respuesta4);
            nextround.setEnabled(true);
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_respuesta4ItemStateChanged

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        bloqueartodo();
        findeljuego();
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(jugar.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(jugar.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(jugar.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(jugar.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new jugar().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel acumuladotxt;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabelRespuesta;
    private javax.swing.JLabel labelrespuesta;
    private javax.swing.JButton nextround;
    private javax.swing.JRadioButton respuesta1;
    private javax.swing.JRadioButton respuesta2;
    private javax.swing.JRadioButton respuesta3;
    private javax.swing.JRadioButton respuesta4;
    // End of variables declaration//GEN-END:variables
}
