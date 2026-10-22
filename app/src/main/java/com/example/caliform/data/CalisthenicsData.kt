package com.example.caliform.data

import com.example.caliform.model.BodyArea
import com.example.caliform.model.DifficultyLevel
import com.example.caliform.model.DurationCategory
import com.example.caliform.model.Equipment
import com.example.caliform.model.Exercise
import com.example.caliform.model.HistoryRecord
import com.example.caliform.model.UserProfile
import com.example.caliform.model.Workout

object CalisthenicsData {

    val sampleUsers = listOf(
        UserProfile(
            id = "user_1",
            name = "Alexandre Silva",
            email = "alexandre.calistenia@aluno.pt",
            studentLevel = "Nível 1 • Aluno Iniciante",
            avatarInitial = "A",
            targetGoal = "Conseguir 15 flexões perfeitas e 5 elevações estritas",
            currentStreakDays = 4,
            totalWorkoutsCompleted = 12,
            totalMinutesTrained = 195,
            rankingPoints = 1450,
            masteredExerciseIds = setOf("pushup_solo", "plank_solo", "squat_solo")
        ),
        UserProfile(
            id = "user_2",
            name = "Tiago Mendes",
            email = "tiago.m@parque.pt",
            studentLevel = "Nível 2 • Praticante Intermédio",
            avatarInitial = "T",
            targetGoal = "Dominar fundos nas barras de 1m e primeiro Muscle-Up",
            currentStreakDays = 8,
            totalWorkoutsCompleted = 38,
            totalMinutesTrained = 740,
            rankingPoints = 3820,
            masteredExerciseIds = setOf(
                "pushup_solo", "plank_solo", "squat_solo", "remada_barra_175",
                "elevacoes_barra_225", "dips_paralelas_1m", "abdominais_banco"
            )
        ),
        UserProfile(
            id = "user_3",
            name = "Gonçalo Ramos",
            email = "goncalo.cali@elite.pt",
            studentLevel = "Nível 3 • Atleta Avançado",
            avatarInitial = "G",
            targetGoal = "10s de Full Planche e Muscle-Up limpo sem balanço",
            currentStreakDays = 15,
            totalWorkoutsCompleted = 82,
            totalMinutesTrained = 2100,
            rankingPoints = 7890,
            masteredExerciseIds = setOf(
                "pushup_solo", "plank_solo", "squat_solo", "remada_barra_175",
                "elevacoes_barra_225", "dips_paralelas_1m", "abdominais_banco",
                "muscle_up_barra_225", "lsit_paralelas_1m", "pistol_squat"
            )
        ),
        UserProfile(
            id = "user_4",
            name = "Marta Ferreira",
            email = "marta.fit@aluno.pt",
            studentLevel = "Nível 1 • Aluno Iniciante",
            avatarInitial = "M",
            targetGoal = "Fortalecimento do core e primeiras elevações na barra 1.75m",
            currentStreakDays = 6,
            totalWorkoutsCompleted = 18,
            totalMinutesTrained = 270,
            rankingPoints = 2100,
            masteredExerciseIds = setOf("pushup_solo", "plank_solo")
        ),
        UserProfile(
            id = "user_5",
            name = "Beatriz Costa",
            email = "beatriz.cali@parque.pt",
            studentLevel = "Nível 2 • Praticante Intermédio",
            avatarInitial = "B",
            targetGoal = "L-Sit 20s nas barras de 1m e flexões arqueiro",
            currentStreakDays = 11,
            totalWorkoutsCompleted = 45,
            totalMinutesTrained = 980,
            rankingPoints = 4650,
            masteredExerciseIds = setOf("pushup_solo", "dips_paralelas_1m", "abdominais_banco")
        ),
        UserProfile(
            id = "user_6",
            name = "Diogo Rocha",
            email = "diogo.r@streetworkout.pt",
            studentLevel = "Nível 3 • Atleta Avançado",
            avatarInitial = "D",
            targetGoal = "Front Lever e 10 Muscle-ups consecutivos",
            currentStreakDays = 19,
            totalWorkoutsCompleted = 95,
            totalMinutesTrained = 2650,
            rankingPoints = 9400,
            masteredExerciseIds = setOf("muscle_up_barra_225", "pistol_squat", "dips_paralelas_1m")
        )
    )

    val exercises = listOf(
        // ================= BARRA 1.75M & SOLO (INICIANTE) =================
        Exercise(
            id = "remada_barra_175",
            title = "Remada Australiana na Barra 1.75m",
            subtitle = "Puxada horizontal na barra baixa para construir costas e preparar elevações",
            difficulty = DifficultyLevel.BEGINNER,
            bodyArea = BodyArea.UPPER_BODY,
            targetRepsOrHold = "3 séries x 8-12 repetições",
            equipmentNeeded = listOf(Equipment.BARRA_175),
            primaryMuscles = listOf("Grande Dorsal", "Rombóides", "Trapézio Médio"),
            secondaryMuscles = listOf("Bíceps", "Deltoide Posterior", "Core"),
            setupSteps = listOf(
                "Posiciona-te debaixo da barra de 1.75m, segurando com as mãos à largura dos ombros.",
                "Caminha com os pés para a frente até o corpo formar uma linha reta diagonal dos calcanhares à cabeça.",
                "Mantém os braços estendidos e ativa o abdómen e os glúteos."
            ),
            executionSteps = listOf(
                "Inicia o movimento juntando as omoplatas (retração escapular).",
                "Puxa o peito em direção à barra de 1.75m, puxando os cotovelos para trás.",
                "Toca com a parte superior do peito na barra e pausa por 1 segundo.",
                "Desce de forma controlada até estender completamente os braços."
            ),
            formCues = listOf(
                "Não deixes a anca descer; o corpo deve mover-se como uma tábua rígida.",
                "Pensa em puxar os cotovelos para trás e esmagar uma noz entre as omoplatas.",
                "Para tornar mais fácil, recua ligeiramente os pés; para dificultar, avança mais os pés."
            ),
            commonMistakes = listOf(
                "Impulsionar com a anca para ajudar a subir.",
                "Não esticar os braços totalmente no fundo da repetição.",
                "Encolher os ombros junto às orelhas."
            ),
            breathingTip = "Expira enquanto o peito sobe até à barra; inspira ao descer com calma.",
            progressionStep = "Passo 1 da Progressão de Puxadas",
            previousProgression = null,
            nextProgression = "elevacoes_barra_225",
            visualType = "pullup"
        ),
        Exercise(
            id = "flexao_barra_175",
            title = "Flexão Inclinada na Barra 1.75m",
            subtitle = "Empurre horizontal com peso reduzido para iniciantes aprenderem a técnica de flexão",
            difficulty = DifficultyLevel.BEGINNER,
            bodyArea = BodyArea.UPPER_BODY,
            targetRepsOrHold = "3 séries x 10-15 repetições",
            equipmentNeeded = listOf(Equipment.BARRA_175),
            primaryMuscles = listOf("Peitoral Maior", "Tríceps"),
            secondaryMuscles = listOf("Deltoide Anterior", "Abdómen"),
            setupSteps = listOf(
                "Coloca as mãos na barra de 1.75m com afastamento ligeiramente superior aos ombros.",
                "Afasta os pés para trás formando uma prancha firme.",
                "Aperta os glúteos e recolhe o umbigo para estabilizar a bacia."
            ),
            executionSteps = listOf(
                "Dobra os cotovelos para trás a cerca de 45 graus, baixando o peito até tocar na barra.",
                "Pausa 1 segundo em contacto com a barra sem apoiar o peso de forma frouxa.",
                "Empurra a barra com força esticando os braços e empurrando as costas no topo."
            ),
            formCues = listOf(
                "Cotovelos apontam para trás em diagonal, nunca abertos em forma de 'T'.",
                "Não deixes a cabeça cair antes do peito.",
                "Mantém todo o corpo em linha reta."
            ),
            commonMistakes = listOf(
                "Lombar curvada ou anca caída.",
                "Afastar demasiado os cotovelos gerando pressão desnecessária no ombro."
            ),
            breathingTip = "Inspira ao descer até à barra; expira ao empurrar para a posição inicial.",
            progressionStep = "Passo 1 da Progressão de Empurre",
            previousProgression = null,
            nextProgression = "pushup_solo",
            visualType = "pushup"
        ),
        Exercise(
            id = "pushup_solo",
            title = "Flexão de Braços no Solo",
            subtitle = "A flexão clássica de calistenia para desenvolvimento de peito, tríceps e estabilidade",
            difficulty = DifficultyLevel.BEGINNER,
            bodyArea = BodyArea.UPPER_BODY,
            targetRepsOrHold = "3-4 séries x 10-15 repetições",
            equipmentNeeded = listOf(Equipment.SOLO),
            primaryMuscles = listOf("Peitoral Maior", "Tríceps", "Deltoide Anterior"),
            secondaryMuscles = listOf("Serrátil Anterior", "Reto Abdominal", "Glúteos"),
            setupSteps = listOf(
                "Mãos no chão à largura dos ombros, dedos ligeiramente abertos para aderência.",
                "Corpo em linha reta dos calcanhares à nuca, pés juntos.",
                "Empurra o chão para afastar as omoplatas (protração escapular)."
            ),
            executionSteps = listOf(
                "Desce o corpo controladamente em 2 segundos até o peito ficar a 2 cm do chão.",
                "Os cotovelos devem viajar a 45 graus do tronco.",
                "Empurra o chão vigorosamente estendendo os cotovelos por completo."
            ),
            formCues = listOf(
                "Aperta as nádegas e o abdómen; o teu corpo é uma prancha única.",
                "Gira as mãos como se quisesses abrir uma tampa de frasco no chão (torque externo).",
                "Toca com o esterno primeiro, não com a barriga."
            ),
            commonMistakes = listOf(
                "Deixar a barriga tocar no chão antes do peito.",
                "Meias repetições sem amplitude total."
            ),
            breathingTip = "Inspira na descida; expira com força na subida.",
            progressionStep = "Passo 2 da Progressão de Empurre",
            previousProgression = "flexao_barra_175",
            nextProgression = "dips_paralelas_1m",
            visualType = "pushup"
        ),

        // ================= BANCO DE ABDOMINAIS =================
        Exercise(
            id = "abdominais_banco",
            title = "Elevação de Tronco no Banco de Abdominais",
            subtitle = "Trabalho profundo na cama/banco de abdominais aproveitando a inclinação",
            difficulty = DifficultyLevel.BEGINNER,
            bodyArea = BodyArea.CORE,
            targetRepsOrHold = "3 séries x 12-15 repetições",
            equipmentNeeded = listOf(Equipment.BANCO_ABDOMINAIS),
            primaryMuscles = listOf("Reto Abdominal", "Oblíquos"),
            secondaryMuscles = listOf("Flexores da Anca", "Transverso"),
            setupSteps = listOf(
                "Deita-te no banco inclinado/cama de abdominais fixando os pés ou pernas no suporte superior.",
                "Mãos cruzadas no peito ou nas têmporas (sem puxar o pescoço).",
                "Enrola a coluna ligeiramente para manter o abdómen já sob tensão."
            ),
            executionSteps = listOf(
                "Contrai o abdómen enrolando a coluna vértebra a vértebra até subir o tronco.",
                "No topo, aperta o abdómen intensamente durante 1 segundo.",
                "Desce de forma lenta e controlada sem deixar o tronco bater com força no banco."
            ),
            formCues = listOf(
                "Enrola o abdómen como um tapete, não subas como uma tábua reta para não puxar apenas a anca.",
                "Mantém o queixo afastado do peito (espaço de uma maçã).",
                "Evita usar balanço dos braços."
            ),
            commonMistakes = listOf(
                "Puxar a cabeça com as mãos forçando a cervical.",
                "Subir de forma balística descolando a lombar com golpe."
            ),
            breathingTip = "Expira na subida enquanto enrolas o tronco; inspira ao descer pelo banco.",
            progressionStep = "Treino de Abdominais no Equipamento Específico",
            previousProgression = null,
            nextProgression = "elevacao_pernas_banco",
            visualType = "lsit"
        ),
        Exercise(
            id = "elevacao_pernas_banco",
            title = "Elevação de Pernas no Banco de Abdominais",
            subtitle = "Deitado na cama de abdominais com pegada na parte superior, focando a zona inferior do abdómen",
            difficulty = DifficultyLevel.INTERMEDIATE,
            bodyArea = BodyArea.CORE,
            targetRepsOrHold = "3 séries x 10-12 repetições",
            equipmentNeeded = listOf(Equipment.BANCO_ABDOMINAIS),
            primaryMuscles = listOf("Reto Abdominal Inferior", "Iliopsoas"),
            secondaryMuscles = listOf("Oblíquos", "Quadríceps"),
            setupSteps = listOf(
                "Deita-te na cama/banco de abdominais segurando firme a barra ou pega por trás da cabeça.",
                "Estica as pernas para a frente mantendo os pés juntos.",
                "Pressiona a zona lombar contra o banco."
            ),
            executionSteps = listOf(
                "Eleva as pernas esticadas até formarem um ângulo de 90 graus com o banco.",
                "No topo, eleva a bacia ligeiramente para fora do banco (curvando a pélvis para o teto).",
                "Desce as pernas de forma suave sem deixar os calcanhares descansarem no fundo."
            ),
            formCues = listOf(
                "A elevação da bacia no topo é o segredo para ativar o abdómen e não apenas a coxa.",
                "Desce em 3 segundos sem balanço.",
                "Se for difícil, começa com os joelhos fletidos (tuck)."
            ),
            commonMistakes = listOf(
                "Arquear a lombar tirando-a do banco ao descer as pernas.",
                "Usar balanço rápido dos pés."
            ),
            breathingTip = "Expira ao elevar as pernas e a bacia; inspira ao descer lentamente.",
            progressionStep = "Nível 2 de Abdominais no Banco",
            previousProgression = "abdominais_banco",
            nextProgression = "dragon_flag_banco",
            visualType = "lsit"
        ),
        Exercise(
            id = "dragon_flag_banco",
            title = "Dragon Flag no Banco de Abdominais",
            subtitle = "O lendário exercício de Bruce Lee executado segurando a barra superior da cama de treino",
            difficulty = DifficultyLevel.ADVANCED,
            bodyArea = BodyArea.CORE,
            targetRepsOrHold = "3 séries x 5-8 repetições ou negativas",
            equipmentNeeded = listOf(Equipment.BANCO_ABDOMINAIS),
            primaryMuscles = listOf("Reto Abdominal Completo", "Dorsais"),
            secondaryMuscles = listOf("Glúteos", "Isquiotibiais", "Tríceps"),
            setupSteps = listOf(
                "Deita-te no banco segurando com firmeza os apoios ou a barra superior atrás da cabeça.",
                "Rola o peso para a parte superior das costas e ombros, elevando o corpo na vertical.",
                "Alinha o corpo como um mastro reto: pés, anca e tronco numa linha só."
            ),
            executionSteps = listOf(
                "Baixa todo o corpo em bloco reto em direção ao banco sem dobrar a anca.",
                "Apenas as omoplatas tocam no banco.",
                "Para quando o corpo estiver a poucos centímetros do banco e sobe de novo usando a força do core."
            ),
            formCues = listOf(
                "Não dobres a cintura; o teu corpo tem de permanecer como uma barra de ferro.",
                "Aperta os glúteos com máxima intensidade.",
                "Se for demasiado exigente, faz apenas a descida negativa de 5 segundos."
            ),
            commonMistakes = listOf(
                "Fletir a anca tornando o exercício numa mera elevação de pernas.",
                "Desabar com a lombar no banco."
            ),
            breathingTip = "Inspira a descer, expira ao subir com contração máxima abdominal.",
            progressionStep = "Domínio Supremo de Core no Banco",
            previousProgression = "elevacao_pernas_banco",
            nextProgression = null,
            visualType = "lsit"
        ),

        // ================= BARRAS PARALELAS 1M =================
        Exercise(
            id = "suporte_paralelas_1m",
            title = "Apoio Estático nas Barras Paralelas de 1m",
            subtitle = "Sustentação do peso corporal com braços esticados para habituar pulsos e ombros",
            difficulty = DifficultyLevel.BEGINNER,
            bodyArea = BodyArea.UPPER_BODY,
            targetRepsOrHold = "3 séries x 20-30 segundos de apoio",
            equipmentNeeded = listOf(Equipment.PARALELAS_1M),
            primaryMuscles = listOf("Tríceps", "Depressores do Ombro", "Peitoral"),
            secondaryMuscles = listOf("Antebraços", "Core"),
            setupSteps = listOf(
                "Sobe para o meio das duas barras paralelas de 1m, uma mão em cada barra.",
                "Estica completamente os braços suportando o teu peso.",
                "Afasta os ombros das orelhas, empurrando as barras para baixo."
            ),
            executionSteps = listOf(
                "Mantém o peito aberto e as pernas esticadas ligeiramente à frente.",
                "Não deixes os ombros subirem em direção ao pescoço.",
                "Sustenta a respiração controlada durante o tempo estipulado."
            ),
            formCues = listOf(
                "Ombros empurrados para baixo com força constante.",
                "Cotovelos totalmente bloqueados em extensão segura.",
                "Olhar fixo em frente."
            ),
            commonMistakes = listOf(
                "Deixar a cabeça afundar entre os ombros.",
                "Fletir ligeiramente os cotovelos perdendo o suporte ósseo."
            ),
            breathingTip = "Respirações curtas e controladas pelo nariz mantendo o abdómen firme.",
            progressionStep = "Base para Fundos nas Paralelas",
            previousProgression = null,
            nextProgression = "dips_paralelas_1m",
            visualType = "dip"
        ),
        Exercise(
            id = "dips_paralelas_1m",
            title = "Fundos (Dips) nas Barras Paralelas de 1m",
            subtitle = "O melhor exercício calisténico para construir peito inferior, tríceps e força de empurre vertical",
            difficulty = DifficultyLevel.INTERMEDIATE,
            bodyArea = BodyArea.UPPER_BODY,
            targetRepsOrHold = "3-4 séries x 8-12 repetições",
            equipmentNeeded = listOf(Equipment.PARALELAS_1M),
            primaryMuscles = listOf("Tríceps Braquial", "Peitoral Maior", "Deltoide Anterior"),
            secondaryMuscles = listOf("Grande Dorsal", "Serrátil Anterior"),
            setupSteps = listOf(
                "Assume a posição de apoio no topo das barras paralelas de 1m.",
                "Inclina o tronco ligeiramente para a frente para recrutar mais o peito.",
                "Cruza os tornozelos ou mantém as pernas juntas esticadas."
            ),
            executionSteps = listOf(
                "Dobra os braços deixando o corpo descer até os cotovelos formarem um ângulo de 90 graus.",
                "Os cotovelos devem viajar para trás, sem abrir para fora.",
                "Pausa brevemente em paralelo e empurra com vigor até bloquear de novo os braços."
            ),
            formCues = listOf(
                "Nunca deixes os ombros colapsarem junto às orelhas.",
                "Cotovelos apontam sempre para trás, paralelos entre si.",
                "Desce até ao ângulo de 90° para proteger a cápsula articular do ombro."
            ),
            commonMistakes = listOf(
                "Cair rápido demais sem controlar a descida.",
                "Balançar o corpo para a frente e para trás para ganhar balanço."
            ),
            breathingTip = "Inspira ao descer; solta o ar com determinação ao empurrar no topo.",
            progressionStep = "Força Fundamental de Paralelas",
            previousProgression = "suporte_paralelas_1m",
            nextProgression = "lsit_paralelas_1m",
            visualType = "dip"
        ),
        Exercise(
            id = "lsit_paralelas_1m",
            title = "L-Sit nas Barras Paralelas de 1m",
            subtitle = "Sustentação isométrica em 'L' com pernas a 90 graus: compressão pura de core e tríceps",
            difficulty = DifficultyLevel.ADVANCED,
            bodyArea = BodyArea.CORE,
            targetRepsOrHold = "4 séries x 15-20 segundos de sustentação",
            equipmentNeeded = listOf(Equipment.PARALELAS_1M),
            primaryMuscles = listOf("Reto Abdominal", "Flexores da Anca", "Tríceps"),
            secondaryMuscles = listOf("Quadríceps", "Depressores da Omoplata"),
            setupSteps = listOf(
                "No topo das barras de 1m, empurra com força os ombros para baixo.",
                "Puxa os joelhos ao peito e em seguida estende as pernas retas para a frente.",
                "Trava os joelhos com a força das coxas."
            ),
            executionSteps = listOf(
                "Mantém as pernas paralelas ao chão, formando um 'L' de 90 graus com o tronco.",
                "Aponta as pontas dos pés para a frente.",
                "Aguenta o tempo sem deixar as pernas caírem abaixo da linha das barras."
            ),
            formCues = listOf(
                "Aperta os quadríceps com força máxima para as pernas não dobrarem.",
                "Empurra as barras para o chão com os braços 100% esticados.",
                "Se não aguentares esticado, mantém os joelhos dobrados a 90° (Tuck L-Sit)."
            ),
            commonMistakes = listOf(
                "Dobrar os cotovelos.",
                "Deixar os pés caírem em direção ao chão."
            ),
            breathingTip = "Respirações curtas sem deixar o peito descer.",
            progressionStep = "Domínio de Força Isométrica nas Paralelas",
            previousProgression = "dips_paralelas_1m",
            nextProgression = null,
            visualType = "lsit"
        ),

        // ================= BARRA 2.25M (ALTA) =================
        Exercise(
            id = "elevacoes_barra_225",
            title = "Elevações Estritas na Barra Alta (2.25m)",
            subtitle = "O rei dos exercícios de calistenia: puxada vertical estrita da suspensão até ao queixo",
            difficulty = DifficultyLevel.INTERMEDIATE,
            bodyArea = BodyArea.UPPER_BODY,
            targetRepsOrHold = "3-4 séries x 6-10 repetições",
            equipmentNeeded = listOf(Equipment.BARRA_225),
            primaryMuscles = listOf("Grande Dorsal", "Bíceps", "Redondo Maior"),
            secondaryMuscles = listOf("Trapézio", "Braquiorradial", "Core"),
            setupSteps = listOf(
                "Segura na barra alta de 2.25m com pegada pronada (palmas para fora) ligeiramente mais larga que os ombros.",
                "Começa em suspensão ativa: ombros afastados das orelhas, pernas esticadas e pés juntos.",
                "Zero balanço corporal antes de iniciar."
            ),
            executionSteps = listOf(
                "Puxa os cotovelos em direção à cintura elevando o corpo de forma contínua.",
                "Leva o peito em direção à barra até o queixo passar nitidamente por cima dela.",
                "Pausa 0.5s no topo com controlo.",
                "Desce em 2-3 segundos até à suspensão completa antes da próxima repetição."
            ),
            formCues = listOf(
                "Pensa em levar o peito à barra, e não apenas esticar o pescoço.",
                "Sem pontapés ou balanços: pura disciplina estrita de calistenia.",
                "Ativa as omoplatas antes de dobrar os cotovelos."
            ),
            commonMistakes = listOf(
                "Balançar com os joelhos para ganhar impulso (kipping).",
                "Descer a cair rápido sem controlar a fase excêntrica."
            ),
            breathingTip = "Expira na subida vigorosa; inspira com calma ao regressar à suspensão.",
            progressionStep = "Passo 2 da Progressão de Puxadas",
            previousProgression = "remada_barra_175",
            nextProgression = "muscle_up_barra_225",
            visualType = "pullup"
        ),
        Exercise(
            id = "elevacao_pernas_barra_225",
            title = "Elevação de Pernas em Suspensão na Barra 2.25m",
            subtitle = "Trabalho de abdómen e descompressão lombar pendurado na barra alta",
            difficulty = DifficultyLevel.INTERMEDIATE,
            bodyArea = BodyArea.CORE,
            targetRepsOrHold = "3 séries x 8-12 repetições",
            equipmentNeeded = listOf(Equipment.BARRA_225),
            primaryMuscles = listOf("Reto Abdominal", "Flexores da Anca"),
            secondaryMuscles = listOf("Antebraços (Pegada)", "Dorsais"),
            setupSteps = listOf(
                "Pendura-te na barra de 2.25m com os braços esticados e ombros ativos.",
                "Garante que o corpo está imóvel sem balançar.",
                "Pernas totalmente esticadas com as pontas dos pés estendidas."
            ),
            executionSteps = listOf(
                "Enrola a bacia para a frente enquanto sobes as pernas esticadas.",
                "Leva os pés até à linha da anca (90 graus) ou até tocarem na barra.",
                "Pausa 1 segundo e desce em 3 segundos controlando a inércia."
            ),
            formCues = listOf(
                "Empurra a barra para a frente com os dorsais para travar qualquer balanço.",
                "Enrola a bacia em direção às costelas; não limites o movimento a mover a coxa."
            ),
            commonMistakes = listOf(
                "Balançar o corpo como um pêndulo.",
                "Deixar as pernas caírem de repente sem resistência."
            ),
            breathingTip = "Expira com força quando as pernas sobem; inspira na descida suave.",
            progressionStep = "Core Vertical em Suspensão",
            previousProgression = "elevacao_pernas_banco",
            nextProgression = null,
            visualType = "pullup"
        ),
        Exercise(
            id = "muscle_up_barra_225",
            title = "Muscle-Up Estrito na Barra Alta (2.25m)",
            subtitle = "A transição definitiva da calistenia: puxar o corpo acima da barra alta num único movimento contínuo",
            difficulty = DifficultyLevel.ADVANCED,
            bodyArea = BodyArea.FULL_BODY_SKILLS,
            targetRepsOrHold = "3-4 séries x 3-6 repetições",
            equipmentNeeded = listOf(Equipment.BARRA_225),
            primaryMuscles = listOf("Grande Dorsal", "Peitoral", "Tríceps", "Ombros"),
            secondaryMuscles = listOf("Antebraços", "Core", "Flexores da Anca"),
            setupSteps = listOf(
                "Grip na barra alta de 2.25m com pegada ligeiramente mais profunda (palmas por cima).",
                "Corpo em suspensão oca (hollow body), pernas juntas e pés em ponta.",
                "Foco na aceleração explosiva."
            ),
            executionSteps = listOf(
                "Puxa com máxima explosão arqueando o peito ao redor da barra (não numa linha reta colada).",
                "Quando o peito passar a altura da barra, lança rapidamente os ombros para a frente por cima dela.",
                "Aterra na posição de fundo e empurra os braços até ao bloqueio completo acima da barra."
            ),
            formCues = listOf(
                "A trajetória da barra é um arco curvilíneo.",
                "Ambos os cotovelos sobem ao mesmo tempo — NUNCA subas um cotovelo primeiro (asa de frango).",
                "Pré-requisito recomendado: 12 elevações estritas ao peito e 15 fundos na barra."
            ),
            commonMistakes = listOf(
                "Subir um braço de cada vez arriscando lesão no manguito rotador.",
                "Falta de velocidade explosiva na primeira puxada."
            ),
            breathingTip = "Expiração explosiva na subida rápida e na transição acima da barra.",
            progressionStep = "Movimento de Maestria em Barra Alta",
            previousProgression = "elevacoes_barra_225",
            nextProgression = null,
            visualType = "muscleup"
        ),

        // ================= SOLO (INFERIORES & ISOMÉTRICOS) =================
        Exercise(
            id = "squat_solo",
            title = "Agachamento Profundo no Solo",
            subtitle = "Base dos membros inferiores para mobilidade de anca, joelhos e força de pernas",
            difficulty = DifficultyLevel.BEGINNER,
            bodyArea = BodyArea.LOWER_BODY,
            targetRepsOrHold = "3-4 séries x 15-20 repetições",
            equipmentNeeded = listOf(Equipment.SOLO),
            primaryMuscles = listOf("Quadríceps", "Glúteo Máximo"),
            secondaryMuscles = listOf("Isquiotibiais", "Gémeos", "Core"),
            setupSteps = listOf(
                "Pés à largura dos ombros, pontas ligeiramente voltadas para fora (15 a 30 graus).",
                "Peito orgulhoso, braços esticados à frente para equilíbrio.",
                "Pés firmes colados ao chão no tripé (calcanhar, base do polegar e mindinho)."
            ),
            executionSteps = listOf(
                "Inicia flexionando a anca e os joelhos ao mesmo tempo, sentando entre os calcanhares.",
                "Desce abaixo da linha paralela (anca abaixo do topo do joelho) com a coluna neutra.",
                "Empurra o chão pelos calcanhares até ficar em pé e contrai os glúteos no topo."
            ),
            formCues = listOf(
                "Calcanhares NUNCA levantam do chão.",
                "Empurra os joelhos para fora na descida e na subida para não colapsarem para dentro.",
                "Mantém o peito aberto e o tronco direito."
            ),
            commonMistakes = listOf(
                "Levantar os calcanhares passando o peso para a ponta dos dedos.",
                "Deixar os joelhos irem para dentro (valgo dinâmico)."
            ),
            breathingTip = "Inspira ao agachar; expira ao empurrar o chão de regresso à posição ereta.",
            progressionStep = "Fundamento de Membros Inferiores",
            previousProgression = null,
            nextProgression = "pistol_squat",
            visualType = "squat"
        ),
        Exercise(
            id = "plank_solo",
            title = "Prancha Hollow no Solo",
            subtitle = "Estabilidade estática anti-extensão fortalecendo todo o cinturão abdominal",
            difficulty = DifficultyLevel.BEGINNER,
            bodyArea = BodyArea.CORE,
            targetRepsOrHold = "3 séries x 45-60 segundos de sustentação",
            equipmentNeeded = listOf(Equipment.SOLO),
            primaryMuscles = listOf("Reto Abdominal", "Transverso"),
            secondaryMuscles = listOf("Oblíquos", "Glúteos", "Serrátil"),
            setupSteps = listOf(
                "Apoia os antebraços no chão com os cotovelos alinhados por baixo dos ombros.",
                "Estica as pernas para trás apoiando na ponta dos pés.",
                "Roda a bacia para trás (retroversão pélvica) e aperta os glúteos."
            ),
            executionSteps = listOf(
                "Empurra o chão com os antebraços arredondando ligeiramente a parte alta das costas.",
                "Puxa isometricamente os cotovelos em direção aos pés para aumentar a tensão.",
                "Mantém a respiração constante e diafragmática sem deixar a bacia descer."
            ),
            formCues = listOf(
                "A tua lombar nunca deve arquear para baixo como uma ponte invertida.",
                "Contrai o abdómen como se fosses levar um toque firme.",
                "Glúteos sempre apertados a 100%."
            ),
            commonMistakes = listOf(
                "Deixar a anca cair em direção ao chão gerando dor lombar.",
                "Subir a anca em formato de tenda para fugir à tensão do abdómen."
            ),
            breathingTip = "Respirações curtas e controladas pelo diafragma mantendo a parede abdominal rígida.",
            progressionStep = "Base de Prancha no Chão",
            previousProgression = null,
            nextProgression = "abdominais_banco",
            visualType = "plank"
        ),
        Exercise(
            id = "pistol_squat",
            title = "Pistol Squat (Agachamento a 1 Perna)",
            subtitle = "A maior prova de equilíbrio, mobilidade de tornozelo e força de pernas da calistenia",
            difficulty = DifficultyLevel.ADVANCED,
            bodyArea = BodyArea.LOWER_BODY,
            targetRepsOrHold = "3-4 séries x 5-8 repetições por perna",
            equipmentNeeded = listOf(Equipment.SOLO),
            primaryMuscles = listOf("Quadríceps", "Glúteos"),
            secondaryMuscles = listOf("Tibial Anterior", "Isquiotibiais", "Core"),
            setupSteps = listOf(
                "Equilibra-te numa perna só, esticando a outra perna no ar em frente.",
                "Estica os braços à frente com os punhos cerrados para contrapeso.",
                "Fixa o olhar num ponto à tua frente."
            ),
            executionSteps = listOf(
                "Desce em controlo na perna de apoio até a coxa tocar no gémeo.",
                "A perna livre permanece no ar sem tocar no chão.",
                "Empurra pelo calcanhar e centro do pé para subir até à extensão completa."
            ),
            formCues = listOf(
                "O calcanhar de apoio tem de ficar totalmente plantado no solo.",
                "Mantém a perna livre bem esticada ativando o quadríceps dessa perna.",
                "Se precisares, apoia uma mão na barra de 1.75m para treinar a descida."
            ),
            commonMistakes = listOf(
                "Desabar no fundo sem controlar a descida.",
                "Deixar o calcanhar levantar do chão."
            ),
            breathingTip = "Inspira a descer, bloqueia a meio e expira com explosão ao subir.",
            progressionStep = "Domínio Unilateral das Pernas",
            previousProgression = "squat_solo",
            nextProgression = null,
            visualType = "squat"
        )
    )

    // ================= ROTINAS ORGANIZADAS POR NÍVEL E DURAÇÃO =================
    // Iniciante: 5 min, 15 min, 1h
    // Intermédio: 5 min, 15 min, 1h
    // Avançado: 5 min, 15 min, 1h
    val workouts = listOf(
        // ---------- INICIANTE ----------
        Workout(
            id = "iniciante_5min",
            title = "Treino 5 min: Ativação & Core",
            subtitle = "Circuito rápido para iniciantes usando o banco de abdominais e o solo",
            durationCategory = DurationCategory.FIVE_MIN,
            durationMinutes = 5,
            difficulty = DifficultyLevel.BEGINNER,
            bodyArea = BodyArea.CORE,
            equipmentNeeded = listOf(Equipment.BANCO_ABDOMINAIS, Equipment.SOLO),
            description = "Treino rápido de 5 minutos ideal para começar o dia ou ativar a musculatura central. Combina abdominais no banco inclinado e prancha no solo sem sobrecarregar as articulações.",
            exerciseIds = listOf("abdominais_banco", "plank_solo", "abdominais_banco"),
            rounds = 2,
            workSecPerExercise = 40,
            restSecBetweenExercises = 15,
            calorieBurnEstimate = 45
        ),
        Workout(
            id = "iniciante_15min",
            title = "Treino 15 min: Circuito do Parque",
            subtitle = "Rotina completa para alunos iniciantes com barra 1.75m, banco e solo",
            durationCategory = DurationCategory.FIFTEEN_MIN,
            durationMinutes = 15,
            difficulty = DifficultyLevel.BEGINNER,
            bodyArea = BodyArea.FULL_BODY_SKILLS,
            equipmentNeeded = listOf(Equipment.BARRA_175, Equipment.BANCO_ABDOMINAIS, Equipment.SOLO),
            description = "O treino perfeito de 15 minutos para quem está a dar os primeiros passos. Trabalha as costas na barra baixa de 1.75m, o peito no solo, as pernas e o core no banco inclinado.",
            exerciseIds = listOf("remada_barra_175", "flexao_barra_175", "abdominais_banco", "squat_solo"),
            rounds = 3,
            workSecPerExercise = 40,
            restSecBetweenExercises = 20,
            calorieBurnEstimate = 135
        ),
        Workout(
            id = "iniciante_1h",
            title = "Treino 1h: Construção de Base Completa",
            subtitle = "Sessão aprofundada de adaptação estrutural e aprendizagem de técnica",
            durationCategory = DurationCategory.SIXTY_MIN,
            durationMinutes = 60,
            difficulty = DifficultyLevel.BEGINNER,
            bodyArea = BodyArea.FULL_BODY_SKILLS,
            equipmentNeeded = listOf(Equipment.BARRA_175, Equipment.PARALELAS_1M, Equipment.BANCO_ABDOMINAIS, Equipment.SOLO),
            description = "Uma hora dedicada a construir a base de força do aluno iniciante. Inclui aquecimento, remadas na barra de 1.75m, apoio nas paralelas de 1m, flexões, agachamentos e abdominais na cama/banco com descanso adequado para garantir a técnica perfeita.",
            exerciseIds = listOf("remada_barra_175", "flexao_barra_175", "suporte_paralelas_1m", "squat_solo", "abdominais_banco", "plank_solo"),
            rounds = 5,
            workSecPerExercise = 45,
            restSecBetweenExercises = 35,
            calorieBurnEstimate = 480
        ),
        Workout(
            id = "iniciante_2h",
            title = "Treino 2h: Maratona de Fundamentos & Mobilidade",
            subtitle = "Sessão longa e metódica de técnica, volume progressivo e respiração",
            durationCategory = DurationCategory.TWO_HOURS,
            durationMinutes = 120,
            difficulty = DifficultyLevel.BEGINNER,
            bodyArea = BodyArea.FULL_BODY_SKILLS,
            equipmentNeeded = listOf(Equipment.BARRA_175, Equipment.BANCO_ABDOMINAIS, Equipment.SOLO, Equipment.PARALELAS_1M),
            description = "Sessão imersiva de 2 horas (120 min) para iniciantes com pausas generosas para aprender cada ângulo. Trabalha remadas na barra 1.75m, flexões suaves, suporte nas paralelas de 1m, abdominais no banco e alongamentos profundos.",
            exerciseIds = listOf("remada_barra_175", "flexao_barra_175", "suporte_paralelas_1m", "squat_solo", "abdominais_banco", "plank_solo"),
            rounds = 8,
            workSecPerExercise = 40,
            restSecBetweenExercises = 45,
            calorieBurnEstimate = 850
        ),

        // ---------- INTERMÉDIO ----------
        Workout(
            id = "intermedio_5min",
            title = "Treino 5 min: Blitz de Paralelas & Banco",
            subtitle = "5 minutos intensos de fundos nas barras de 1m e elevação no banco",
            durationCategory = DurationCategory.FIVE_MIN,
            durationMinutes = 5,
            difficulty = DifficultyLevel.INTERMEDIATE,
            bodyArea = BodyArea.UPPER_BODY,
            equipmentNeeded = listOf(Equipment.PARALELAS_1M, Equipment.BANCO_ABDOMINAIS),
            description = "Treino express para alunos intermédios que têm pouco tempo. Foco em força de tríceps nas barras paralelas de 1m e contração abdominal no banco inclinado.",
            exerciseIds = listOf("dips_paralelas_1m", "elevacao_pernas_banco", "dips_paralelas_1m"),
            rounds = 2,
            workSecPerExercise = 35,
            restSecBetweenExercises = 15,
            calorieBurnEstimate = 65
        ),
        Workout(
            id = "intermedio_15min",
            title = "Treino 15 min: Força Clássica das Barras",
            subtitle = "Elevações na barra alta 2.25m, fundos nas paralelas 1m e abdominais",
            durationCategory = DurationCategory.FIFTEEN_MIN,
            durationMinutes = 15,
            difficulty = DifficultyLevel.INTERMEDIATE,
            bodyArea = BodyArea.FULL_BODY_SKILLS,
            equipmentNeeded = listOf(Equipment.BARRA_225, Equipment.PARALELAS_1M, Equipment.BANCO_ABDOMINAIS),
            description = "A combinação de ouro da calistenia intermédia. Elevações estritas na barra alta de 2.25m alternadas com fundos nas paralelas de 1m e elevação de pernas no banco inclinado.",
            exerciseIds = listOf("elevacoes_barra_225", "dips_paralelas_1m", "elevacao_pernas_banco", "pushup_solo"),
            rounds = 3,
            workSecPerExercise = 45,
            restSecBetweenExercises = 25,
            calorieBurnEstimate = 165
        ),
        Workout(
            id = "intermedio_1h",
            title = "Treino 1h: Potência & Volume Total",
            subtitle = "Sessão rigorosa explorando todos os equipamentos do parque de treino",
            durationCategory = DurationCategory.SIXTY_MIN,
            durationMinutes = 60,
            difficulty = DifficultyLevel.INTERMEDIATE,
            bodyArea = BodyArea.FULL_BODY_SKILLS,
            equipmentNeeded = listOf(Equipment.BARRA_225, Equipment.PARALELAS_1M, Equipment.BANCO_ABDOMINAIS, Equipment.SOLO),
            description = "Treino completo de 60 minutos para alunos de nível intermédio. Volume substancial de elevações na barra de 2.25m, fundos pesados nas paralelas de 1m, elevação de pernas na barra alta e no banco, e agachamentos profundos.",
            exerciseIds = listOf("elevacoes_barra_225", "dips_paralelas_1m", "elevacao_pernas_barra_225", "elevacao_pernas_banco", "pushup_solo", "squat_solo"),
            rounds = 5,
            workSecPerExercise = 45,
            restSecBetweenExercises = 30,
            calorieBurnEstimate = 540
        ),
        Workout(
            id = "intermedio_2h",
            title = "Treino 2h: Resistência e Volume Máximo no Parque",
            subtitle = "Maratona muscular de elevações, fundos nas paralelas e pernas",
            durationCategory = DurationCategory.TWO_HOURS,
            durationMinutes = 120,
            difficulty = DifficultyLevel.INTERMEDIATE,
            bodyArea = BodyArea.FULL_BODY_SKILLS,
            equipmentNeeded = listOf(Equipment.BARRA_225, Equipment.PARALELAS_1M, Equipment.BANCO_ABDOMINAIS, Equipment.SOLO),
            description = "Uma verdadeira maratona de calistenia de 2 horas (120 min). Volume alto de puxadas na barra alta de 2.25m, empurres nas paralelas de 1m, flexões pliométricas e elevação de pernas no banco inclinado.",
            exerciseIds = listOf("elevacoes_barra_225", "dips_paralelas_1m", "elevacao_pernas_banco", "pushup_solo", "squat_solo", "elevacao_pernas_barra_225"),
            rounds = 8,
            workSecPerExercise = 45,
            restSecBetweenExercises = 40,
            calorieBurnEstimate = 1050
        ),

        // ---------- AVANÇADO ----------
        Workout(
            id = "avancado_5min",
            title = "Treino 5 min: Ativação de Elite (L-Sit & Dragon)",
            subtitle = "5 minutos de tensão extrema nas paralelas de 1m e cama de abdominais",
            durationCategory = DurationCategory.FIVE_MIN,
            durationMinutes = 5,
            difficulty = DifficultyLevel.ADVANCED,
            bodyArea = BodyArea.CORE,
            equipmentNeeded = listOf(Equipment.PARALELAS_1M, Equipment.BANCO_ABDOMINAIS),
            description = "Densidade pura de tensão estática. Alternância entre a sustentação de L-Sit nas barras paralelas de 1m e a descida controlada de Dragon Flag no banco.",
            exerciseIds = listOf("lsit_paralelas_1m", "dragon_flag_banco", "lsit_paralelas_1m"),
            rounds = 2,
            workSecPerExercise = 30,
            restSecBetweenExercises = 20,
            calorieBurnEstimate = 70
        ),
        Workout(
            id = "avancado_15min",
            title = "Treino 15 min: Força Explosiva & Muscle-Up",
            subtitle = "Muscle-up na barra alta de 2.25m, fundos fundos e pistol squats",
            durationCategory = DurationCategory.FIFTEEN_MIN,
            durationMinutes = 15,
            difficulty = DifficultyLevel.ADVANCED,
            bodyArea = BodyArea.FULL_BODY_SKILLS,
            equipmentNeeded = listOf(Equipment.BARRA_225, Equipment.PARALELAS_1M, Equipment.SOLO),
            description = "15 minutos de alto rendimento atlético. Combina muscle-ups limpos na barra alta de 2.25m, fundos nas paralelas e agachamentos em pistola sem apoio.",
            exerciseIds = listOf("muscle_up_barra_225", "dips_paralelas_1m", "pistol_squat", "lsit_paralelas_1m"),
            rounds = 3,
            workSecPerExercise = 40,
            restSecBetweenExercises = 30,
            calorieBurnEstimate = 180
        ),
        Workout(
            id = "avancado_1h",
            title = "Treino 1h: Domínio Supremo das Barras",
            subtitle = "Sessão completa de calistenia pesada, habilidades e resistência muscular",
            durationCategory = DurationCategory.SIXTY_MIN,
            durationMinutes = 60,
            difficulty = DifficultyLevel.ADVANCED,
            bodyArea = BodyArea.FULL_BODY_SKILLS,
            equipmentNeeded = listOf(Equipment.BARRA_225, Equipment.PARALELAS_1M, Equipment.BANCO_ABDOMINAIS, Equipment.SOLO),
            description = "O teste definitivo do atleta. 1 hora de treino de elite passando pelos Muscle-Ups na barra de 2.25m, fundos pesados nas paralelas de 1m, Dragon Flags no banco de abdominais, Pistol Squats e L-Sit.",
            exerciseIds = listOf("muscle_up_barra_225", "dips_paralelas_1m", "dragon_flag_banco", "pistol_squat", "lsit_paralelas_1m", "elevacoes_barra_225"),
            rounds = 5,
            workSecPerExercise = 45,
            restSecBetweenExercises = 35,
            calorieBurnEstimate = 620
        ),
        Workout(
            id = "avancado_2h",
            title = "Treino 2h: Sessão Épica de Habilidades & Força Extrema",
            subtitle = "Desafio lendário de Muscle-Ups, Dragon Flags, L-Sit e Isometrias",
            durationCategory = DurationCategory.TWO_HOURS,
            durationMinutes = 120,
            difficulty = DifficultyLevel.ADVANCED,
            bodyArea = BodyArea.FULL_BODY_SKILLS,
            equipmentNeeded = listOf(Equipment.BARRA_225, Equipment.PARALELAS_1M, Equipment.BANCO_ABDOMINAIS, Equipment.SOLO),
            description = "O pináculo da calistenia atlética: 2 horas de alta exigência com séries densas de Muscle-Ups na barra 2.25m, fundos profundos nas paralelas de 1m, Dragon Flag na cama de abdominais, Pistol Squats e sustentação estática.",
            exerciseIds = listOf("muscle_up_barra_225", "dips_paralelas_1m", "dragon_flag_banco", "pistol_squat", "lsit_paralelas_1m", "elevacoes_barra_225"),
            rounds = 8,
            workSecPerExercise = 45,
            restSecBetweenExercises = 45,
            calorieBurnEstimate = 1250
        )
    )

    fun getStudentInitialHistory(userId: String): List<HistoryRecord> {
        return when (userId) {
            "user_1" -> listOf( // Alexandre Silva - Iniciante
                HistoryRecord(
                    id = "hist_alex_1",
                    workoutTitle = "Treino 15 min: Primeiros Passos no Parque",
                    dateFormatted = "Ontem, 18:30",
                    durationMinutes = 15,
                    completedExerciseNames = listOf("Remada Australiana na Barra 1.75m", "Flexão Inclinada na Barra 1.75m", "Elevação de Tronco no Banco de Abdominais"),
                    difficulty = DifficultyLevel.BEGINNER,
                    ratingStars = 5,
                    notes = "Sensação excelente nas remadas e controle abdominal no banco.",
                    pointsEarned = 250
                ),
                HistoryRecord(
                    id = "hist_alex_2",
                    workoutTitle = "Treino 5 min: Ativação & Core",
                    dateFormatted = "Há 2 dias, 08:15",
                    durationMinutes = 5,
                    completedExerciseNames = listOf("Elevação de Tronco no Banco de Abdominais", "Prancha Hollow no Solo"),
                    difficulty = DifficultyLevel.BEGINNER,
                    ratingStars = 4,
                    notes = "Ativação matinal rápida.",
                    pointsEarned = 100
                ),
                HistoryRecord(
                    id = "hist_alex_3",
                    workoutTitle = "Treino 15 min: Primeiros Passos no Parque",
                    dateFormatted = "Há 4 dias, 19:00",
                    durationMinutes = 15,
                    completedExerciseNames = listOf("Remada Australiana na Barra 1.75m", "Agachamento Profundo no Solo"),
                    difficulty = DifficultyLevel.BEGINNER,
                    ratingStars = 5,
                    notes = "Cadência controlada e bom descanso.",
                    pointsEarned = 250
                )
            )
            "user_2" -> listOf( // Tiago Mendes - Intermédio
                HistoryRecord(
                    id = "hist_tiago_1",
                    workoutTitle = "Treino 1h: Potência & Volume Total",
                    dateFormatted = "Ontem, 17:45",
                    durationMinutes = 60,
                    completedExerciseNames = listOf("Elevação Estrita na Barra 2.25m", "Fundos nas Barras Paralelas de 1m", "Elevação de Pernas no Banco"),
                    difficulty = DifficultyLevel.INTERMEDIATE,
                    ratingStars = 5,
                    notes = "Volume alto com boa explosão nos fundos.",
                    pointsEarned = 1105
                ),
                HistoryRecord(
                    id = "hist_tiago_2",
                    workoutTitle = "Treino 15 min: Força Clássica das Barras",
                    dateFormatted = "Há 2 dias, 18:00",
                    durationMinutes = 15,
                    completedExerciseNames = listOf("Elevação Estrita na Barra 2.25m", "Fundos nas Barras Paralelas de 1m"),
                    difficulty = DifficultyLevel.INTERMEDIATE,
                    ratingStars = 5,
                    notes = "Repetições estritas sem balanço.",
                    pointsEarned = 325
                ),
                HistoryRecord(
                    id = "hist_tiago_3",
                    workoutTitle = "Treino 30 min: Resistência Intermédia",
                    dateFormatted = "Há 3 dias, 19:15",
                    durationMinutes = 30,
                    completedExerciseNames = listOf("Fundos nas Barras Paralelas de 1m", "Elevação de Pernas no Banco"),
                    difficulty = DifficultyLevel.INTERMEDIATE,
                    ratingStars = 4,
                    notes = "Foco em contração escapular.",
                    pointsEarned = 585
                )
            )
            "user_3" -> listOf( // Gonçalo Ramos - Avançado
                HistoryRecord(
                    id = "hist_goncalo_1",
                    workoutTitle = "Treino 2h: Sessão Épica de Habilidades & Força Extrema",
                    dateFormatted = "Ontem, 16:30",
                    durationMinutes = 120,
                    completedExerciseNames = listOf("Muscle-Up Limpo na Barra 2.25m", "Dragon Flag na Cama de Abdominais", "L-Sit 20s nas Paralelas 1m", "Pistol Squat"),
                    difficulty = DifficultyLevel.ADVANCED,
                    ratingStars = 5,
                    notes = "Sessão épica de 2 horas. 8 rounds completados sem falha de forma.",
                    pointsEarned = 2000
                ),
                HistoryRecord(
                    id = "hist_goncalo_2",
                    workoutTitle = "Treino 1h: Domínio Supremo das Barras",
                    dateFormatted = "Há 2 dias, 17:00",
                    durationMinutes = 60,
                    completedExerciseNames = listOf("Muscle-Up Limpo na Barra 2.25m", "Fundos nas Barras Paralelas de 1m", "Dragon Flag na Cama de Abdominais"),
                    difficulty = DifficultyLevel.ADVANCED,
                    ratingStars = 5,
                    notes = "Técnica limpa e alta densidade.",
                    pointsEarned = 1360
                ),
                HistoryRecord(
                    id = "hist_goncalo_3",
                    workoutTitle = "Treino 15 min: Força Explosiva & Muscle-Up",
                    dateFormatted = "Há 3 dias, 18:30",
                    durationMinutes = 15,
                    completedExerciseNames = listOf("Muscle-Up Limpo na Barra 2.25m", "L-Sit nas Paralelas 1m"),
                    difficulty = DifficultyLevel.ADVANCED,
                    ratingStars = 5,
                    notes = "Transição explosiva acima da barra.",
                    pointsEarned = 400
                )
            )
            else -> listOf(
                HistoryRecord(
                    id = "hist_default_1",
                    workoutTitle = "Treino 15 min: Primeiros Passos no Parque",
                    dateFormatted = "Ontem, 18:00",
                    durationMinutes = 15,
                    completedExerciseNames = listOf("Remada Australiana na Barra 1.75m", "Elevação de Tronco no Banco de Abdominais", "Flexões no Solo"),
                    difficulty = DifficultyLevel.BEGINNER,
                    ratingStars = 5,
                    notes = "Primeira sessão concluída com sucesso.",
                    pointsEarned = 250
                )
            )
        }
    }

    val sampleHistory = getStudentInitialHistory("user_1")
}
