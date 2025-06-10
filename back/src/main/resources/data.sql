-- Insertion des topics
INSERT INTO topics (id, title, description, created_at, updated_at) VALUES 
(1,'Angular','Framework JavaScript pour création d\'applications web','2024-09-29 14:58:18','2024-09-29 14:58:18'),
(2,'React','Bibliothèque JavaScript pour interfaces utilisateur','2024-09-29 14:58:18','2024-09-29 14:58:18'),
(3,'Vue.js','Framework progressif pour construire des interfaces utilisateur','2024-09-29 14:58:18','2024-09-29 14:58:18'),
(4,'Node.js','Environnement d\'exécution JavaScript côté serveur','2024-09-29 14:58:18','2024-09-29 14:58:18'),
(5,'Python','Langage de programmation polyvalent et puissant','2024-09-29 14:58:18','2024-09-29 14:58:18'),
(6,'Machine Learning','Sous-domaine de l\'intelligence artificielle','2024-09-29 14:58:18','2024-09-29 14:58:18');

-- Insertion des users
INSERT INTO users (id, email, username, password, created_at, updated_at) VALUES 
(1,'utilisateur1@gmail.com','utilisateur1','$2a$10$loGgh7sBrUj800QvJq9PLuT8pOQ.pD41PRRfsT3wqovmTUNG3S8Ya','2024-09-27 13:39:35','2024-09-27 13:39:35'),
(2,'utilisateur2@gmail.com','utilisateur2','$2a$10$mNGxaRcsT6fl4Ntws//okewC4BQRwPGl0HnctJapOB8Zs7wCpwxpO','2024-09-30 17:15:47','2024-09-30 17:15:47'),
(3,'utilisateur3@gmail.com','utilisateur3','$2a$10$1VNHJFRt5/539FrxAhemEe6fvrYqWRjawfMefb8w0xXksKWcY7YAy','2024-09-30 17:17:02','2024-09-30 17:17:02'),
(4,'utilisateur4@gmail.com','utilisateur4','$2a$10$R802CggR.jqQ.e2L44VR9OxBVbYLkE7y.ofz7uVCOxMRq1a6dPvj.','2024-09-30 17:17:19','2024-09-30 17:17:19'),
(5,'utilisateur5@gmail.com','utilisateur5','$2a$10$iHj0QnSWl96B9fXEacXhXepycqQTBEjLpZ7qABZRt5qSt/vl570Wu','2024-09-30 17:17:40','2024-09-30 17:17:40'),
(6,'utilisateur6@gmail.com','utilisateur6','$2a$10$0uRHHDgUChVIMsWLsM9VDuaJWKTWAJDMQn82xJVld6P6kyKfJ96XO','2024-09-30 17:17:59','2024-09-30 17:17:59');

-- Insertion des articles
INSERT INTO articles (id, author_id, topic_id, title, content, created_at, updated_at) VALUES 
(25,4,1,'Introduction à Angular','Angular est un framework JavaScript complet qui permet de créer des applications web complexes de manière structurée et modulaire.','2023-12-19 23:00:00','2024-03-11 23:00:00'),
(26,5,2,'Pourquoi choisir React ?','React est une bibliothèque JavaScript maintenue par Facebook pour construire des interfaces utilisateur.','2024-06-16 22:00:00','2024-03-09 23:00:00'),
(27,2,3,'Vue.js : un framework léger et flexible','Vue.js est un framework JavaScript progressif qui facilite la création d\'interfaces utilisateur.','2023-12-10 23:00:00','2024-07-22 22:00:00'),
(28,2,4,'Découvrir Node.js','Node.js est un environnement d\'exécution JavaScript côté serveur.','2024-09-05 22:00:00','2024-04-23 22:00:00'),
(29,2,5,'Python : le langage polyvalent','Python est un langage de programmation polyvalent et puissant.','2024-04-03 22:00:00','2024-06-10 22:00:00'),
(30,2,6,'Introduction au Machine Learning','Le Machine Learning est une branche de l\'intelligence artificielle.','2024-06-12 22:00:00','2024-01-24 23:00:00');

-- Insertion des subscriptions
INSERT INTO user_topic_subscriptions (user_id, topic_id) VALUES 
(1,1),(6,1),(6,2),(6,3),(1,4),(1,5);

-- Insertion des commentaires
INSERT INTO comments (id, article_id, author_id, content, created_at, updated_at) VALUES 
(1,25,2,'Super article sur Angular, j\'ai appris beaucoup de choses !','2024-07-15 08:35:21','2024-07-15 08:35:21'),
(2,25,4,'Je suis d\'accord avec les points abordés.','2024-07-16 12:12:09','2024-07-16 12:15:42'),
(3,26,1,'React est vraiment impressionnant, merci pour cet article détaillé !','2024-08-02 07:45:32','2024-08-02 07:45:32'),
(4,26,3,'Bien expliqué, mais l\'exemple pourrait être plus complet.','2024-08-03 15:32:58','2024-08-03 15:45:12'),
(5,27,6,'Vue.js a un design très intuitif et est facile à prendre en main.','2024-04-12 10:25:14','2024-04-12 10:30:10');