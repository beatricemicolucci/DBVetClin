INSERT INTO padrone (CF, Nome, Cognome, DataNascita, Indirizzo, Telefono, IndirizzoEmail) VALUES ('RSSMRC85C15L402U', 'Marco', 'Russo', '1985-03-15', 'Via Mazzini 8', '3805512148', 'marco.russo@hotmail.com');
INSERT INTO padrone (CF, Nome, Cognome, DataNascita, Indirizzo, Telefono, IndirizzoEmail) VALUES ('FRRLRA90D65F203J', 'Laura', 'Ferrari', '1990-09-25', 'Via Garibaldi 12', '3921559865', 'laura.ferrari@gmail.com');
INSERT INTO padrone (CF, Nome, Cognome, DataNascita, Indirizzo, Telefono, IndirizzoEmail) VALUES ('ESPLCU87E02G501K', 'Luca', 'Esposito', '1987-07-02', 'Via Dante 15', '3778554213', 'luca.esposito@gmail.com');
INSERT INTO padrone (CF, Nome, Cognome, DataNascita, Indirizzo, Telefono, IndirizzoEmail) VALUES ('RZZMRT83H41A000R', 'Marta', 'Rizzo', '1983-11-23', 'Via Carducci 20', '3804596751', 'marta.rizzo@yahoo.com');
INSERT INTO padrone (CF, Nome, Cognome, DataNascita, Indirizzo, Telefono, IndirizzoEmail) VALUES ('CNTGLI95B54C101A', 'Giulia', 'Conti', '1995-06-17', 'Via Monti 3', '3926998745', ' giulia.conti@hotmail.com');
INSERT INTO padrone (CF, Nome, Cognome, DataNascita, Indirizzo, Telefono, IndirizzoEmail) VALUES ('LNGLSI88C18D409A', 'Alessio', 'Longo', '1988-09-28', 'Via Leopardi 6', '3778461265', ' alessio.longo@gmail.com');
INSERT INTO padrone (CF, Nome, Cognome, DataNascita, Indirizzo, Telefono, IndirizzoEmail) VALUES ('MRCSRA92D65F502T', 'Sara', 'Marchetti', '1992-07-12', 'Via Garibaldi 9', '3205887964', 'sara.marchetti@yahoo.com');

INSERT INTO veterinario (CodImpiegato, Ambulatorio, CF, Nome, Cognome, DataNascita, Indirizzo, Telefono, IndirizzoEmail) VALUES (3, '10.4', 'DLCMTT79P20H501X', 'Matteo', 'De Luca', '1979-02-20', 'Via Volta 7', '3248446525', 'matteo.deluca@hotmail.com');
INSERT INTO veterinario (CodImpiegato, Ambulatorio, CF, Nome, Cognome, DataNascita, Indirizzo, Telefono, IndirizzoEmail) VALUES (4, '4.8', 'MRTGNN66H26F839J', 'Giovanni', 'Moretti', '1966-06-26', 'Via Vestina 105', '3204512965', null);
INSERT INTO veterinario (CodImpiegato, Ambulatorio, CF, Nome, Cognome, DataNascita, Indirizzo, Telefono, IndirizzoEmail) VALUES (5, '13', 'FRNLSI77S11H501I', 'Lisa', 'Farina', '1977-11-11', 'Via Emilia 45', '3294786215', 'lisa.farina@libero.it');

INSERT INTO specializzazione (Ambito) VALUES ('Radiologia veterinaria');
INSERT INTO specializzazione (Ambito) VALUES ('Oncologia veterinaria');
INSERT INTO specializzazione (Ambito) VALUES ('Dermatologia veterinaria');
INSERT INTO specializzazione (Ambito) VALUES ('Neurologia veterinaria');
INSERT INTO specializzazione (Ambito) VALUES ('Riproduzione e ginecologia veterinaria');
INSERT INTO specializzazione (Ambito) VALUES ('Chirurgia veterinaria');

INSERT INTO competenza (AmbitoSpecializzazione, CodVeterinario) VALUES ('Radiologia veterinaria', 4);
INSERT INTO competenza (AmbitoSpecializzazione, CodVeterinario) VALUES ('Oncologia veterinaria', 4);
INSERT INTO competenza (AmbitoSpecializzazione, CodVeterinario) VALUES ('Riproduzione e ginecologia veterinaria', 5);
INSERT INTO competenza (AmbitoSpecializzazione, CodVeterinario) VALUES ('Neurologia veterinaria', 3);
INSERT INTO competenza (AmbitoSpecializzazione, CodVeterinario) VALUES ('Chirurgia veterinaria', 3);

INSERT INTO animale (Microchip, Nome, Razza, DataNascita, Sesso, CF_Padrone) VALUES (4, 'Rocky', 'Golden Retriever', '2020-06-05', 'M', 'MRCSRA92D65F502T');
INSERT INTO animale (Microchip, Nome, Razza, DataNascita, Sesso, CF_Padrone) VALUES (5, 'Daisy', 'Beagle', '2018-09-15', 'F', 'RZZMRT83H41A000R');
INSERT INTO animale (Microchip, Nome, Razza, DataNascita, Sesso, CF_Padrone) VALUES (6, 'Oliver', 'Bulldog Francese', '2015-10-20', 'M', 'ESPLCU87E02G501K');
INSERT INTO animale (Microchip, Nome, Razza, DataNascita, Sesso, CF_Padrone) VALUES (7, 'Lily', 'Poodle', '2022-03-30', 'F', 'CNTGLI95B54C101A');
INSERT INTO animale (Microchip, Nome, Razza, DataNascita, Sesso, CF_Padrone) VALUES (8, 'Cooper', 'Labrador Retriever', '2011-05-08', 'M', 'LNGLSI88C18D409A');
INSERT INTO animale (Microchip, Nome, Razza, DataNascita, Sesso, CF_Padrone) VALUES (9, 'Lola', 'Maltese', '2021-12-23', 'F', 'FRRLRA90D65F203J');
INSERT INTO animale (Microchip, Nome, Razza, DataNascita, Sesso, CF_Padrone) VALUES (10, 'Duke', 'Yorkshire Terrier', '2014-08-19', 'M', 'RSSMRC85C15L402U');

INSERT INTO cartella_clinica(CodiceCartella, CodAnimale, DataCreazione) VALUES (4, 4, curdate());
INSERT INTO cartella_clinica(CodiceCartella, CodAnimale, DataCreazione) VALUES (5, 5, curdate());
INSERT INTO cartella_clinica(CodiceCartella, CodAnimale, DataCreazione) VALUES (6, 6, curdate());
INSERT INTO cartella_clinica(CodiceCartella, CodAnimale, DataCreazione) VALUES (7, 7, curdate());
INSERT INTO cartella_clinica(CodiceCartella, CodAnimale, DataCreazione) VALUES (8, 8, curdate());
INSERT INTO cartella_clinica(CodiceCartella, CodAnimale, DataCreazione) VALUES (9, 9, curdate());
INSERT INTO cartella_clinica(CodiceCartella, CodAnimale, DataCreazione) VALUES (10, 10, curdate());

INSERT INTO fattura(NumeroFattura, DataFattura, Spesa, Servizi, CF_Padrone) VALUES (3, '2023-10-15', 200.00, 'Esame ginecologico', 'RZZMRT83H41A000R');
INSERT INTO fattura(NumeroFattura, DataFattura, Spesa, Servizi, CF_Padrone) VALUES (4, '2024-01-10', 150.00, 'Chirurgia oftalmica', 'LNGLSI88C18D409A');
INSERT INTO fattura(NumeroFattura, DataFattura, Spesa, Servizi, CF_Padrone) VALUES (5, '2023-08-01', 210.00, 'Chirurgia gastrointestinale', 'MRCSRA92D65F502T');
INSERT INTO fattura(NumeroFattura, DataFattura, Spesa, Servizi, CF_Padrone) VALUES (6, '2023-07-31', 100.00, 'Radiografia', 'ESPLCU87E02G501K');
INSERT INTO fattura(NumeroFattura, DataFattura, Spesa, Servizi, CF_Padrone) VALUES (7, '2023-05-05', 90.00, 'Test allergologici', 'ESPLCU87E02G501K');
INSERT INTO fattura(NumeroFattura, DataFattura, Spesa, Servizi, CF_Padrone) VALUES (8, '2023-09-10', 200.00, 'Visita di controllo', 'ESPLCU87E02G501K');
INSERT INTO fattura(NumeroFattura, DataFattura, Spesa, Servizi, CF_Padrone) VALUES (9, '2022-03-15', 170.00, 'Endoscopia', 'FRRLRA90D65F203J');
INSERT INTO fattura(NumeroFattura, DataFattura, Spesa, Servizi, CF_Padrone) VALUES (11, '2022-04-07', 50.00, 'Vaccinazione', 'RSSMRC85C15L402U');
INSERT INTO fattura(NumeroFattura, DataFattura, Spesa, Servizi, CF_Padrone) VALUES (12, '2022-03-20', 300.00, 'Chirurgia cardiaca', 'CNTGLI95B54C101A');

INSERT INTO esame (CodEsame, NumeroFattura, Tipologia, Giorno, OraInizio, OraFine, CodVeterinario, CodiceCartella) VALUES (2, 3, 'Esame ginecologico', '2023-10-15', '10:00:00', '11:00:00', 5, 5);
INSERT INTO esame (CodEsame, NumeroFattura, Tipologia, Giorno, OraInizio, OraFine, CodVeterinario, CodiceCartella) VALUES (3, 6, 'Radiografia', '2023-07-31', '15:00:00', '16:00:00', 4, 6);
INSERT INTO esame (CodEsame, NumeroFattura, Tipologia, Giorno, OraInizio, OraFine, CodVeterinario, CodiceCartella) VALUES (4, 7, 'Test allergologici', '2023-05-05', '09:00:00', '11:00:00', 2, 6);
INSERT INTO esame (CodEsame, NumeroFattura, Tipologia, Giorno, OraInizio, OraFine, CodVeterinario, CodiceCartella) VALUES (5, 9, 'Endoscopia', '2022-03-15', '16:00:00', '17:30:00', 3, 9);

INSERT INTO controllo (CodVeterinario, Giorno, OraInizio, NumeroFattura, OraFine, CodiceCartella) VALUES (1, '2023-09-10','17:00:00', 8, '18:00:00', 6);

INSERT INTO vaccinazione (CodVeterinario, Giorno, OraInizio, NumeroFattura, OraFine, Malattia, CodiceCartella) VALUES (2, '2022-04-07', '10:00:00', 11, '10:30:00', 'Epatite infettiva canina', 10);

INSERT INTO sala_operatoria (CodiceSala) VALUES(10);
INSERT INTO sala_operatoria (CodiceSala) VALUES(3);
INSERT INTO sala_operatoria (CodiceSala) VALUES(5);

INSERT INTO intervento (CodiceSala, Tipo, Giorno, OraInizio, NumeroFattura, OraFine, CodVeterinario, CodiceCartella) VALUES (10, 'Chirurgia oftalmica', '2024-01-10', '14:00:00', 4, '16:00:00', 3, 8);
INSERT INTO intervento (CodiceSala, Tipo, Giorno, OraInizio, NumeroFattura, OraFine, CodVeterinario, CodiceCartella) VALUES (3, 'Chirurgia gastrointestinale', '2023-08-01', '15:00:00', 5, '17:00:00', 3, 4);
INSERT INTO intervento (CodiceSala, Tipo, Giorno, OraInizio, NumeroFattura, OraFine, CodVeterinario, CodiceCartella) VALUES (5, 'Chirurgia cardiaca', '2022-03-20', '10:00:00', 12, '13:00:00', 4, 7);

INSERT INTO malattia (Descrizione, CodVeterinario, GiornoControllo, OraInizioControllo, CodEsame) VALUES ('Iperplasia endometriale', null, null, null, 2);
INSERT INTO malattia (Descrizione, CodVeterinario, GiornoControllo, OraInizioControllo, CodEsame) VALUES ('Allergia al mais', null, null, null, 4);
INSERT INTO malattia (Descrizione, CodVeterinario, GiornoControllo, OraInizioControllo, CodEsame) VALUES ('Ulcera cervicale', null, null, null, 5);
INSERT INTO malattia (Descrizione, CodVeterinario, GiornoControllo, OraInizioControllo, CodEsame) VALUES ('Aritmia cardiaca', 1, '2023-09-10','17:00:00', null);

INSERT INTO terapia (CodiceTerapia, DescrizioneMalattia, CodVeterinario, CodiceCartella) VALUES (1, 'Iperplasia endometriale', 5, 5);
INSERT INTO terapia (CodiceTerapia, DescrizioneMalattia, CodVeterinario, CodiceCartella) VALUES (2, 'Allergia al mais', 2, 6);
INSERT INTO terapia (CodiceTerapia, DescrizioneMalattia, CodVeterinario, CodiceCartella) VALUES (3, 'Ulcera cervicale', 3, 9);
INSERT INTO terapia (CodiceTerapia, DescrizioneMalattia, CodVeterinario, CodiceCartella) VALUES (4, 'Aritmia cardiaca', 1, 6);

INSERT INTO farmaco (CodFarmaco) VALUES (1);
INSERT INTO farmaco (CodFarmaco) VALUES (2);
INSERT INTO farmaco (CodFarmaco) VALUES (3);
INSERT INTO farmaco (CodFarmaco) VALUES (4);

INSERT INTO composizione (CodFarmaco, CodiceTerapia) VALUES (1,1);
INSERT INTO composizione (CodFarmaco, CodiceTerapia) VALUES (2,2);
INSERT INTO composizione (CodFarmaco, CodiceTerapia) VALUES (3,3);
INSERT INTO composizione (CodFarmaco, CodiceTerapia) VALUES (4,4);