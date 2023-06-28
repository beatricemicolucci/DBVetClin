-- *********************************************
-- * SQL MySQL generation                      
-- *--------------------------------------------
-- * DB-MAIN version: 11.0.2              
-- * Generator date: Sep 14 2021              
-- * Generation date: Wed Jun 21 10:24:55 2023 
-- * LUN file: C:\Users\user\OneDrive\Documenti\AA-Beatrice\AA-Uni\Basi di dati\PROGETTO\PROGETTO_DB.lun 
-- * Schema: Clinica_veterinaria/1 
-- ********************************************* 


-- Database Section
-- ________________ 

create database Clinica_veterinaria;
use Clinica_veterinaria;


-- Tables Section
-- _____________ 

create table ALLERGIA (
     IdAllergia int not null auto_increment,
     Descrizione varchar(30) not null,
     constraint IDALLERGIA primary key (IdAllergia));

create table ALLERGIE_CARTELLA (
     CodiceCartella int not null,
     IdAllergia int not null,
     constraint IDALLERGIE_CARTELLA primary key (IdAllergia, CodiceCartella));

create table ANIMALE (
     Microchip int not null auto_increment,
     Nome varchar(20) not null,
     Razza varchar(30) not null,
     DataNascita date not null,
     Sesso char(1) not null,
     CF_Padrone char(16) not null,
     constraint IDANIMALE_ID primary key (Microchip));

create table ASSISTENZA (
     CodTecnico int not null,
     CodVeterinario int not null,
     constraint IDassistenza primary key (CodTecnico, CodVeterinario));

create table CARTELLA_CLINICA (
     CodiceCartella int not null auto_increment ,
     CodAnimale int not null,
     DataCreazione date not null,
     constraint IDCARTELLACLINICA primary key (CodiceCartella));

create table CONTROLLO (
     CodVeterinario int not null,
     Giorno date not null,
     OraInizio time not null,
     NumeroFattura int not null,
     OraFine time not null,
     CodiceCartella int not null,
     constraint IDCONTROLLO primary key (CodVeterinario, Giorno, OraInizio));


create table ESAME (
     CodEsame int not null auto_increment,
     NumeroFattura int not null,
     Tipologia varchar(30) not null,
     Giorno date not null,
     OraInizio time not null,
     OraFine time not null,
     CodVeterinario int not null,
	 CodiceCartella int not null,
     constraint IDESAME primary key (CodEsame));

create table FARMACO (
     CodFarmaco int not null auto_increment,
     constraint IDFARMACO_ID primary key (CodFarmaco));

create table FATTURA (
     NumeroFattura int not null auto_increment,
     DataFattura date not null,
     Spesa float(6) not null,
     Servizi varchar(50) not null,
     CF_Padrone char(16) not null,
     constraint IDFATTURA_ID primary key (NumeroFattura));

create table INTERVENTO (
     CodiceSala int not null,
	 Tipo varchar(20) not null,
	 Giorno date not null,
     OraInizio time not null,
     NumeroFattura int not null,
     OraFine time not null,
     CodVeterinario int not null,
     CodiceCartella int not null,
     constraint IDINTERVENTO primary key (Giorno, OraInizio, CodiceSala));

create table MALATTIA (
     Descrizione varchar(30) not null,
     CodVeterinario int not null,
     GiornoControllo date,
     OraInizioControllo time,
     CodEsame int,
     constraint IDMALATTIA_ID primary key (Descrizione));

create table COMPETENZA (
     AmbitoSpecializzazione varchar(50) not null,
     CodVeterinario int not null,
     constraint IDCOMPETENZA primary key (AmbitoSpecializzazione, CodVeterinario));

create table COMPOSIZIONE (
     CodFarmaco int not null,
     CodiceTerapia int not null,
     constraint IDCOMPOSIZIONE primary key (CodFarmaco, CodiceTerapia));
     
create table PADRONE (
     CF char(16) not null,
     Nome varchar(20) not null,
     Cognome varchar(20) not null,
     DataNascita date not null,
     Indirizzo varchar(30) not null,
     Telefono char(10) not null,
     IndirizzoEmail varchar(20),
     constraint IDPADRONE_ID primary key (CF));

create table INGREDIENTE (
     NomeScientifico varchar(30) not null,
     CodFarmaco int not null,
     constraint IDIngredienti primary key (NomeScientifico));
     
create table RICOVERO (
     DataInizio date not null,
     DataFine date not null,
     CodiceRicovero int not null auto_increment,
     GiornoIntervento date not null,
     OraInizioIntervento time not null,
     CodiceSalaInt int not null,
     constraint IDRICOVERO primary key (CodiceRicovero));

create table SALA_OPERATORIA (
     CodiceSala int not null auto_increment,
     constraint IDSALA_OPERATORIA primary key (CodiceSala));

create table SPECIALIZZAZIONE (
     Ambito varchar(50) not null,
     constraint IDSPECIALIZZAZIONE primary key (Ambito));
    
create table TECNICO (
     CodImpiegato int not null auto_increment,
     CF char(16) not null,
     Nome varchar(20) not null,
     Cognome varchar(20) not null,
     DataNascita date not null,
     Indirizzo varchar(30) not null,
     Telefono char(10) not null,
     IndirizzoEmail varchar(20),
     constraint IDTECNICO primary key (CodImpiegato));

create table TERAPIA (
     CodiceTerapia int not null auto_increment,
     DescrizioneMalattia varchar(30) not null,
     CodVeterinario int not null,
     CodiceCartella int not null,
     constraint IDTERAPIA_ID primary key (CodiceTerapia));

create table VACCINAZIONE (
     CodVeterinario int not null,
     Giorno date not null,
     OraInizio time not null,
     NumeroFattura int not null,
     OraFine time not null,
     Malattia varchar(30) not null,
     CodiceCartella int not null,
     constraint IDVACCINAZIONE primary key (CodVeterinario, Giorno, OraInizio));

create table VETERINARIO (
     CodImpiegato int not null auto_increment,
     Ambulatorio varchar(20) not null,
     CF char(16) not null,
     Nome varchar(20) not null,
     Cognome varchar(20) not null,
     DataNascita date not null,
     Indirizzo varchar(30) not null,
     Telefono char(10) not null,
     IndirizzoEmail varchar(20),
     constraint IDVETERINARIO_1_ID primary key (CodImpiegato));

-- Constraints Section
-- ___________________ 

alter table ALLERGIE_CARTELLA add constraint FKCAR_ALL_1
     foreign key (IdAllergia)
     references ALLERGIA (IdAllergia);

alter table ALLERGIE_CARTELLA add constraint FKCAR_CAR
     foreign key (CodiceCartella)
     references CARTELLA_CLINICA (CodiceCartella);

-- Not implemented
-- alter table ANIMALE add constraint IDANIMALE_CHK
--     check(exists(select * from CARTELLA CLINICA
--                  where CARTELLA CLINICA.CodAnimale = Microchip)); 

alter table ANIMALE add constraint FKproprietà
     foreign key (CF_Padrone)
     references PADRONE (CF);

alter table ASSISTENZA add constraint FKass_VET
     foreign key (CodVeterinario)
     references VETERINARIO (CodImpiegato);

alter table ASSISTENZA add constraint FKass_TEC
     foreign key (CodTecnico)
     references TECNICO (CodImpiegato);

alter table CARTELLA_CLINICA add constraint FKarchivio
     foreign key (CodAnimale)
     references ANIMALE (Microchip);

alter table CONTROLLO add constraint FKeffettuazioneCon
     foreign key (CodVeterinario)
     references VETERINARIO (CodImpiegato);

alter table CONTROLLO add constraint FKfattCon
     foreign key (NumeroFattura)
     references FATTURA (NumeroFattura);

alter table CONTROLLO add constraint FKregCon
     foreign key (CodiceCartella)
     references CARTELLA_CLINICA (CodiceCartella);

alter table ESAME add constraint FKeffettuazioneEs
     foreign key (CodVeterinario)
     references VETERINARIO (CodImpiegato);

alter table ESAME add constraint FKfatturazioneEs
     foreign key (NumeroFattura)
     references FATTURA (NumeroFattura);

alter table ESAME add constraint FKregistrazioneEs
     foreign key (CodiceCartella)
     references CARTELLA_CLINICA (CodiceCartella);

-- Not implemented
-- alter table FARMACO add constraint IDFARMACO_CHK
--     check(exists(select * from INGREDIENTE
--                  where INGREDIENTE.CodFarmaco = CodFarmaco)); 

-- Not implemented
-- alter table FATTURA add constraint IDFATTURA_CHK
--     check(exists(select * from VACCINAZIONE
--                  where VACCINAZIONE.NumeroFattura = NumeroFattura)); 

-- Not implemented
-- alter table FATTURA add constraint IDFATTURA_CHK
--     check(exists(select * from CONTROLLO
--                  where CONTROLLO.NumeroFattura = NumeroFattura)); 

-- Not implemented
-- alter table FATTURA add constraint IDFATTURA_CHK
--     check(exists(select * from ESAME
--                  where ESAME.NumeroFattura = NumeroFattura)); 

-- Not implemented
-- alter table FATTURA add constraint IDFATTURA_CHK
--     check(exists(select * from INTERVENTO
--                  where INTERVENTO.NumeroFattura = NumeroFattura)); 

alter table FATTURA add constraint FKricevuta
     foreign key (CF_Padrone)
     references PADRONE (CF);

alter table INTERVENTO add constraint FKluogo
     foreign key (CodiceSala)
     references SALA_OPERATORIA (CodiceSala);

alter table INTERVENTO add constraint FKeffettuazioneInt
     foreign key (CodVeterinario)
     references VETERINARIO (CodImpiegato);

alter table INTERVENTO add constraint FKfatturazioneInt
     foreign key (NumeroFattura)
     references FATTURA (NumeroFattura);

alter table INTERVENTO add constraint FKregistrazioneInt
     foreign key (CodiceCartella)
     references CARTELLA_CLINICA (CodiceCartella);

-- Not implemented
-- alter table MALATTIA add constraint IDMALATTIA_CHK
--     check(exists(select * from TERAPIA
--                  where TERAPIA.DescrizioneMalattia = Descrizione)); 

alter table MALATTIA add constraint FKdiagnosi_FK
     foreign key (CodVeterinario, GiornoControllo, OraInizioControllo)
     references CONTROLLO (CodVeterinario, Giorno, OraInizio);

alter table MALATTIA add constraint FKdiagnosi_CHK
     check((CodVeterinario is not null and GiornoControllo is not null and OraInizioControllo is not null)
           or (CodVeterinario is null and GiornoControllo is null and OraInizioControllo is null)); 

alter table MALATTIA add constraint FKresponso
     foreign key (CodEsame)
     references ESAME (CodEsame);

alter table COMPETENZA add constraint FKcom_VET
     foreign key (CodVeterinario)
     references VETERINARIO (CodImpiegato);

alter table COMPETENZA add constraint FKcom_SPE
     foreign key (AmbitoSpecializzazione)
     references SPECIALIZZAZIONE (Ambito);

alter table COMPOSIZIONE add constraint FKcom_FAR
     foreign key (CodFarmaco)
     references FARMACO (CodFarmaco);

alter table COMPOSIZIONE add constraint FKcom_TER
     foreign key (CodiceTerapia)
     references TERAPIA (CodiceTerapia);

-- Not implemented
-- alter table PADRONE add constraint IDPADRONE_CHK
--     check(exists(select * from ANIMALE
--                  where ANIMALE.CFPadrone = CF)); 

-- Not implemented
-- alter table PADRONE add constraint IDPADRONE_CHK
--     check(exists(select * from Telefono
--                  where Telefono.CFPadrone = CF)); 

alter table INGREDIENTE add constraint FKFAR_Ing
     foreign key (CodFarmaco)
     references FARMACO (CodFarmaco);

alter table RICOVERO add constraint FKrichiesta
     foreign key (GiornoIntervento, OraInizioIntervento, CodiceSalaInt)
     references INTERVENTO (Giorno, OraInizio, CodiceSala);

-- Not implemented
-- alter table TECNICO add constraint IDTECNICO_CHK
--     check(exists(select * from Telefono
--                  where Telefono.CodTecnico = CodImpiegato)); 

-- Not implemented
-- alter table TERAPIA add constraint IDTERAPIA_CHK
--     check(exists(select * from COMPOSIZIONE
--                  where COMPOSIZIONE.CodiceTerapia = CodiceTerapia)); 

alter table TERAPIA add constraint FKprescrizione
     foreign key (CodVeterinario)
     references VETERINARIO (CodImpiegato);

alter table TERAPIA add constraint FKtrattamento
     foreign key (CodiceCartella)
     references CARTELLA_CLINICA (CodiceCartella);

alter table TERAPIA add constraint FKcura
     foreign key (DescrizioneMalattia)
     references MALATTIA (Descrizione);

alter table VACCINAZIONE add constraint FKeffettVac
     foreign key (CodVeterinario)
     references VETERINARIO (CodImpiegato);

alter table VACCINAZIONE add constraint FKfattVac
     foreign key (NumeroFattura)
     references FATTURA (NumeroFattura);

alter table VACCINAZIONE add constraint FKregVac
     foreign key (CodiceCartella)
     references CARTELLA_CLINICA (CodiceCartella);

-- Not implemented
-- alter table VETERINARIO add constraint IDVETERINARIO_1_CHK
--     check(exists(select * from Telefono
--                  where Telefono.CodVeterinario = CodImpiegato)); 


-- Index Section
-- _____________ 

