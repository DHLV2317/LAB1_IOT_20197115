package com.example.lab1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class QuestionBank {

    /** Devuelve 7 preguntas barajadas para el tema indicado. */
    public static List<Question> getFor(String topic) {
        List<Question> qs = new ArrayList<>();

        if ("Redes".equalsIgnoreCase(topic)) {
            // 7 preguntas de Redes
            qs.add(q("¿Qué capa del modelo OSI utiliza el protocolo IP?",
                    "Red", "Enlace de datos", "Transporte", "Aplicación", 0));

            qs.add(q("¿Cuál es el puerto TCP estándar de HTTP?",
                    "80", "21", "110", "443", 0));

            qs.add(q("¿Qué dispositivo interconecta redes y enruta paquetes?",
                    "Router", "Switch", "Hub", "Repetidor", 0));

            qs.add(q("¿Qué protocolo asigna direcciones IP de forma dinámica?",
                    "DHCP", "DNS", "ARP", "ICMP", 0));

            qs.add(q("¿Cuál NO es una red privada?",
                    "8.8.8.0/24", "10.0.0.0/8", "172.16.0.0/12", "192.168.0.0/16", 0));

            qs.add(q("¿Qué protocolo es confiable y orientado a conexión?",
                    "TCP", "UDP", "IP", "ARP", 0));

            qs.add(q("Una máscara /24 equivale a:",
                    "255.255.255.0", "255.255.0.0", "255.0.0.0", "255.255.255.128", 0));

        } else if ("Ciberseguridad".equalsIgnoreCase(topic)) {
            // 7 preguntas de Ciberseguridad
            qs.add(q("¿Qué hace principalmente un firewall?",
                    "Filtra tráfico según reglas", "Cifra discos", "Hace copias de seguridad", "Compila código", 0));

            qs.add(q("¿Qué describe mejor la autenticación de dos factores (2FA)?",
                    "Usar dos factores distintos (p. ej., contraseña y código de app)",
                    "Usar la misma contraseña dos veces",
                    "Cambiar la contraseña cada día",
                    "Compartir la contraseña con el equipo", 0));

            qs.add(q("¿Cuál de los siguientes es un cifrado simétrico?",
                    "AES", "RSA", "SHA-256", "Diffie-Hellman", 0));

            qs.add(q("¿Qué es el phishing?",
                    "Engaño para obtener credenciales vía correos o mensajes falsos",
                    "Ataque por fuerza bruta al router",
                    "Un tipo de software antivirus",
                    "Una técnica de compresión de datos", 0));

            qs.add(q("¿Para qué se usa una función hash en seguridad?",
                    "Verificar la integridad de la información",
                    "Cifrar de extremo a extremo",
                    "Autenticar usuarios por biometría",
                    "Balancear carga entre servidores", 0));

            qs.add(q("El principio de mínimo privilegio indica que se debe:",
                    "Otorgar solo los permisos estrictamente necesarios",
                    "Dar permisos de administrador por defecto",
                    "Permitir todo y bloquear si hay incidente",
                    "Compartir cuentas para simplificar acceso", 0));

            qs.add(q("¿Qué es buena práctica para contraseñas?",
                    "Usar gestor y contraseñas largas únicas",
                    "Reutilizar la misma contraseña",
                    "Apuntarlas en notas visibles",
                    "Usar solo letras minúsculas", 0));

        } else { // "Microondas"
            // 7 preguntas de Microondas / RF
            qs.add(q("¿En qué rangos de frecuencia operan comúnmente las redes Wi-Fi?",
                    "2.4 GHz y 5 GHz", "10 GHz y 20 GHz", "900 MHz y 1.8 GHz", "440 MHz y 550 MHz", 0));

            qs.add(q("Si aumenta la frecuencia f, la longitud de onda λ = c/f:",
                    "Disminuye", "Aumenta", "No cambia", "Oscila aleatoriamente", 0));

            qs.add(q("¿Qué antena se usa típicamente para enlaces punto a punto en microondas?",
                    "Parabólica (dish)", "Dipolo", "Loop magnética", "Yagi de TV doméstica", 0));

            qs.add(q("¿Qué efecto atenúa fuertemente señales alrededor de 60 GHz?",
                    "Absorción por oxígeno", "Reflexión en la ionosfera", "Dispersión por lluvia ligera", "Ruido galáctico", 0));

            qs.add(q("¿Qué esquema de modulación emplea Wi-Fi moderno (802.11a/g/n/ac)?",
                    "OFDM", "FSK", "AM", "PWM", 0));

            qs.add(q("¿Cuál es la velocidad de la luz c usada en λ = c/f?",
                    "≈ 3×10^8 m/s", "≈ 3×10^6 m/s", "≈ 3×10^10 m/s", "≈ 3×10^3 m/s", 0));

            qs.add(q("¿Cuál de estas bandas es ISM (uso industrial, científico y médico)?",
                    "2.4 GHz", "1.2 GHz", "400 GHz", "60 kHz", 0));
        }

        // Barajar preguntas y también las opciones manteniendo el índice correcto
        Collections.shuffle(qs);
        for (Question q : qs) {
            String correct = q.options.get(q.correctIndex);
            Collections.shuffle(q.options);
            for (int i = 0; i < q.options.size(); i++) {
                if (q.options.get(i).equals(correct)) {
                    q.correctIndex = i;
                    break;
                }
            }
        }

        // Limitar a 7 por partida (por si se añaden más en el futuro)
        return qs.size() > 7 ? new ArrayList<>(qs.subList(0, 7)) : qs;
    }

    // Helper para construir una pregunta
    private static Question q(String p, String a, String b, String c, String d, int ok){
        Question Q = new Question();
        Q.prompt = p;
        Q.options = new ArrayList<>(Arrays.asList(a, b, c, d));
        Q.correctIndex = ok; // índice respecto a (a,b,c,d) ANTES de barajar
        return Q;
    }
}