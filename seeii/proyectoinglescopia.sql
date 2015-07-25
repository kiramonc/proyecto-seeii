-- phpMyAdmin SQL Dump
-- version 4.3.11
-- http://www.phpmyadmin.net
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 24-07-2015 a las 21:30:37
-- Versión del servidor: 5.6.24
-- Versión de PHP: 5.6.8

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Base de datos: `proyectoinglescopia`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `administrador`
--

CREATE TABLE IF NOT EXISTS `administrador` (
  `idAdmin` int(11) NOT NULL,
  `usuarioAdmin` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `administrador`
--

INSERT INTO `administrador` (`idAdmin`, `usuarioAdmin`) VALUES
(1, 1),
(2, 2),
(3, 5),
(4, 6),
(5, 7);

--
-- Disparadores `administrador`
--
DELIMITER $$
CREATE TRIGGER `trggBeforeInsertAdministrador` BEFORE INSERT ON `administrador`
 FOR EACH ROW begin
set @ultimoCodigo=(select max(idAdmin) from administrador);
if @ultimoCodigo is null then
	set @ultimoCodigo=0;
end if;
set @codigo=(@ultimoCodigo)+1;
set NEW.idAdmin=(select @codigo);
end
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `concepto`
--

CREATE TABLE IF NOT EXISTS `concepto` (
  `idConcepto` int(11) NOT NULL,
  `nombreConcepto` varchar(100) NOT NULL,
  `traduccion` varchar(100) NOT NULL,
  `descripcion` varchar(100) NOT NULL,
  `estado` tinyint(1) NOT NULL,
  `tema` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `concepto`
--

INSERT INTO `concepto` (`idConcepto`, `nombreConcepto`, `traduccion`, `descripcion`, `estado`, `tema`) VALUES
(0, 'hermano', 'brother', 'aefaw', 1, 2),
(1, 'hermana', 'sister', 'sdfawe', 1, 2);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `estudiante`
--

CREATE TABLE IF NOT EXISTS `estudiante` (
  `idEst` int(11) NOT NULL,
  `usuarioEst` int(11) NOT NULL,
  `estUnidEnsen` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `estudiante`
--

INSERT INTO `estudiante` (`idEst`, `usuarioEst`, `estUnidEnsen`) VALUES
(1, 3, 1),
(2, 4, 1);

--
-- Disparadores `estudiante`
--
DELIMITER $$
CREATE TRIGGER `trggBeforeInsertEstudiante` BEFORE INSERT ON `estudiante`
 FOR EACH ROW begin
set @ultimoCodigo=(select max(idEst) from estudiante);
if @ultimoCodigo is null then
	set @ultimoCodigo=0;
end if;
set @codigo=(@ultimoCodigo)+1;
set NEW.idEst=(select @codigo);
end
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `ficha`
--

CREATE TABLE IF NOT EXISTS `ficha` (
  `idFicha` int(11) NOT NULL,
  `nombreFicha` varchar(40) NOT NULL,
  `descripcion` varchar(100) DEFAULT NULL,
  `imagen` longblob,
  `sonido` longblob,
  `temaFicha` int(11) NOT NULL,
  `estadoAprendizaje` varchar(50) NOT NULL,
  `estado` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `fichainterrogacion`
--

CREATE TABLE IF NOT EXISTS `fichainterrogacion` (
  `idFichaInt` int(11) NOT NULL,
  `ficha` int(11) NOT NULL,
  `interrogacion` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `interrogacion`
--

CREATE TABLE IF NOT EXISTS `interrogacion` (
  `idInt` int(11) NOT NULL,
  `fecha` date NOT NULL,
  `testInterrogacion` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `item`
--

CREATE TABLE IF NOT EXISTS `item` (
  `idItem` int(11) NOT NULL,
  `nombreItem` varchar(200) NOT NULL,
  `imgItem` varchar(150) NOT NULL,
  `traduccion` varchar(200) NOT NULL,
  `preguntaItem` int(11) NOT NULL,
  `estado` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `item`
--

INSERT INTO `item` (`idItem`, `nombreItem`, `imgItem`, `traduccion`, `preguntaItem`, `estado`) VALUES
(1, 'sister', 'business-woman.png', 'hermana', 1, 1);

--
-- Disparadores `item`
--
DELIMITER $$
CREATE TRIGGER `trggBeforeInsertItem` BEFORE INSERT ON `item`
 FOR EACH ROW begin
set @ultimoCodigo=(select max(idItem) from item);
if @ultimoCodigo is null then
	set @ultimoCodigo=0;
end if;
set @codigo=(@ultimoCodigo)+1;
set NEW.idItem=(select @codigo);
end
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `pregunta`
--

CREATE TABLE IF NOT EXISTS `pregunta` (
  `idPregunta` int(11) NOT NULL,
  `descripcion` varchar(250) NOT NULL,
  `peso` double NOT NULL,
  `testPreg` int(11) NOT NULL,
  `conceptoPreg` int(11) NOT NULL,
  `estado` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `pregunta`
--

INSERT INTO `pregunta` (`idPregunta`, `descripcion`, `peso`, `testPreg`, `conceptoPreg`, `estado`) VALUES
(1, '¿Cuál es la traducción de hermana?', 2, 1, 1, 1);

--
-- Disparadores `pregunta`
--
DELIMITER $$
CREATE TRIGGER `trggBeforeInsertPregunta` BEFORE INSERT ON `pregunta`
 FOR EACH ROW begin
set @ultimoCodigo=(select max(idPregunta) from pregunta);
if @ultimoCodigo is null then
	set @ultimoCodigo=0;
end if;
set @codigo=(@ultimoCodigo)+1;
set NEW.idPregunta=(select @codigo);
end
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `resultado`
--

CREATE TABLE IF NOT EXISTS `resultado` (
  `idResultado` int(11) NOT NULL,
  `testResultado` int(11) NOT NULL,
  `estResultado` int(11) NOT NULL,
  `fechaResultado` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `rol`
--

CREATE TABLE IF NOT EXISTS `rol` (
  `tipo` varchar(20) NOT NULL,
  `descripcion` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `rol`
--

INSERT INTO `rol` (`tipo`, `descripcion`) VALUES
('Administrador', NULL),
('Estudiante', NULL);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `tema`
--

CREATE TABLE IF NOT EXISTS `tema` (
  `idTema` int(11) NOT NULL,
  `nombre` varchar(40) NOT NULL,
  `vocabulario` varchar(100) NOT NULL,
  `TemUnidEnsen` int(11) NOT NULL,
  `objetivo` varchar(100) DEFAULT NULL,
  `dominio` varchar(100) DEFAULT NULL,
  `estado` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `tema`
--

INSERT INTO `tema` (`idTema`, `nombre`, `vocabulario`, `TemUnidEnsen`, `objetivo`, `dominio`, `estado`) VALUES
(2, 'Family', 'sister', 1, 'ejemplo tema', 'asfrwae', 1),
(3, 'Sustantivos', 'house', 2, 'asdfawe', 'cualquiera', 1),
(4, 'Jobs', 'Singer', 1, 'conocer las profesiones', 'asdfawe', 1),
(5, 'Grammar', 'to be', 2, 'sdfwe', 'añwef', 1),
(6, 'School', 'pencil, table, teacher', 1, 'Conocer el entorno de la escuela', 'speak', 1),
(7, 'Grammar 1', 'future', 2, 'conjugar el futuro', 'listening', 1);

--
-- Disparadores `tema`
--
DELIMITER $$
CREATE TRIGGER `trggBeforeInsertTema` BEFORE INSERT ON `tema`
 FOR EACH ROW begin
set @ultimoCodigo=(select max(idTema) from tema);
if @ultimoCodigo is null then
	set @ultimoCodigo=0;
end if;
set @codigo=(@ultimoCodigo)+1;
set NEW.idTema=(select @codigo);
end
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `test`
--

CREATE TABLE IF NOT EXISTS `test` (
  `idTest` int(11) NOT NULL,
  `temaTest` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `test`
--

INSERT INTO `test` (`idTest`, `temaTest`) VALUES
(1, 2),
(2, 3),
(3, 6),
(4, 7);

--
-- Disparadores `test`
--
DELIMITER $$
CREATE TRIGGER `trggBeforeInsertTest` BEFORE INSERT ON `test`
 FOR EACH ROW begin
set @ultimoCodigo=(select max(idTest) from test);
if @ultimoCodigo is null then
	set @ultimoCodigo=0;
end if;
set @codigo=(@ultimoCodigo)+1;
set NEW.idTest=(select @codigo);
end
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `testemparejamiento`
--

CREATE TABLE IF NOT EXISTS `testemparejamiento` (
  `idEntrena` int(11) NOT NULL,
  `error` int(11) NOT NULL,
  `tiempo` int(11) NOT NULL,
  `puntaje` int(11) NOT NULL,
  `fecha` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `unidadensenianza`
--

CREATE TABLE IF NOT EXISTS `unidadensenianza` (
  `id` int(11) NOT NULL,
  `nombreUnidad` varchar(40) NOT NULL,
  `adminUnidEnsen` int(11) NOT NULL,
  `estado` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `unidadensenianza`
--

INSERT INTO `unidadensenianza` (`id`, `nombreUnidad`, `adminUnidEnsen`, `estado`) VALUES
(1, 'Unidad Básica', 1, 1),
(2, 'Unit one', 1, 1);

--
-- Disparadores `unidadensenianza`
--
DELIMITER $$
CREATE TRIGGER `trggBeforeInsertUnidadE` BEFORE INSERT ON `unidadensenianza`
 FOR EACH ROW begin
set @ultimoCodigo=(select max(id) from unidadensenianza);
if @ultimoCodigo is null then
	set @ultimoCodigo=0;
end if;
set @codigo=(@ultimoCodigo)+1;
set NEW.id=(select @codigo);
end
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuario`
--

CREATE TABLE IF NOT EXISTS `usuario` (
  `idUsuario` int(11) NOT NULL,
  `nombre` varchar(30) NOT NULL,
  `apellido` varchar(30) NOT NULL,
  `fechaNacimiento` date NOT NULL,
  `genero` tinyint(1) NOT NULL,
  `username` varchar(25) NOT NULL,
  `password` varchar(200) NOT NULL,
  `estado` tinyint(1) NOT NULL,
  `rolUsuario` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `usuario`
--

INSERT INTO `usuario` (`idUsuario`, `nombre`, `apellido`, `fechaNacimiento`, `genero`, `username`, `password`, `estado`, `rolUsuario`) VALUES
(1, 'Katherine', 'Ramon', '1992-11-04', 0, 'kiramonc', '192855c961bfb7184d5dabb6b371bcc49d304f966de0429dfacc5af4a830a38ab8fc6f731dd1a66edf1904ec09548079a981da755b6da00a41e8ef7a6f1f852c', 1, 'Administrador'),
(2, 'Silvia', 'Vacacela', '2015-07-10', 0, 'silvia', '3ef109fe55e194a0f37b5936405dcf2b96ce7eeb8891d59650c708d26f256d018fd8f98c6efb7da8f85029a3708e34f86a54e700ba3b36fb0967aa869ed37915', 1, 'Administrador'),
(3, 'Rosmery', 'Anahi', '2015-07-05', 0, 'rosmery', 'c32ee455f6df75c05c88172c8810649ffda5ee19692bc1396d744bf5a306dd757f20e52e5958b3c9bd27c6d6057f6dfe9b8b072f4bc529a5b472187ff3ad8525', 1, 'Estudiante'),
(4, 'juan', 'carpio', '2015-07-01', 1, 'juan', '673d4b1d7deabe33d0037d3a39927ec3d56397a45f5eb9ac0512c75808c293f0d022e04adc5555cd3644d18cf79e9e9ebaea7e3a8e96744b0c49312a7f8af398', 0, 'Estudiante'),
(5, 'roberto', 'jacome', '2015-07-14', 1, 'roberto', '9f3d2d9e76ce4013d97a5f8d4fbacd4cd25649b65853fb35b0561a1b3e02c6e4a8d9fba76766f003631477c696f26e00206bcf8e6b1d051175cfffb2a9f723a7', 0, 'Administrador'),
(6, 'mario', 'palma', '2015-07-14', 1, 'mario', '76bb849338db38e0ede3b8ae726373c42992152747c39e484f096b623946c8a265adde3a72c8177a70a8876694b9403f06d44decfcfe44be25f1078be0282239', 0, 'Administrador'),
(7, 'faef', 'ñiyvi', '2015-07-23', 1, 'faef', '0a4ca22ae175f571183eff7d553b2fe3790a1717f18c2fd3491df9b5b7aec1a102c132da14bb7807cad333d642bbf6d2139326ab7d22106df389d1450175b7d8', 0, 'Administrador');

--
-- Disparadores `usuario`
--
DELIMITER $$
CREATE TRIGGER `trggBeforeInsertUsuario` BEFORE INSERT ON `usuario`
 FOR EACH ROW begin
set @ultimoCodigo=(select max(idUsuario) from Usuario);
if @ultimoCodigo is null then
	set @ultimoCodigo=0;
end if;
set @codigo=(@ultimoCodigo)+1;
set NEW.idUsuario=(select @codigo);
end
$$
DELIMITER ;

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `administrador`
--
ALTER TABLE `administrador`
  ADD PRIMARY KEY (`idAdmin`), ADD KEY `idAdmin` (`usuarioAdmin`);

--
-- Indices de la tabla `concepto`
--
ALTER TABLE `concepto`
  ADD PRIMARY KEY (`idConcepto`), ADD KEY `idTema` (`tema`);

--
-- Indices de la tabla `estudiante`
--
ALTER TABLE `estudiante`
  ADD PRIMARY KEY (`idEst`), ADD KEY `idUsuario` (`usuarioEst`), ADD KEY `id` (`estUnidEnsen`);

--
-- Indices de la tabla `ficha`
--
ALTER TABLE `ficha`
  ADD PRIMARY KEY (`idFicha`), ADD KEY `idTema` (`temaFicha`);

--
-- Indices de la tabla `fichainterrogacion`
--
ALTER TABLE `fichainterrogacion`
  ADD PRIMARY KEY (`idFichaInt`), ADD KEY `idFicha` (`ficha`), ADD KEY `idInt` (`interrogacion`);

--
-- Indices de la tabla `interrogacion`
--
ALTER TABLE `interrogacion`
  ADD PRIMARY KEY (`idInt`), ADD KEY `idEntrena` (`testInterrogacion`);

--
-- Indices de la tabla `item`
--
ALTER TABLE `item`
  ADD PRIMARY KEY (`idItem`), ADD KEY `idPregunta` (`preguntaItem`);

--
-- Indices de la tabla `pregunta`
--
ALTER TABLE `pregunta`
  ADD PRIMARY KEY (`idPregunta`), ADD KEY `idTest` (`testPreg`), ADD KEY `idConcepto` (`conceptoPreg`);

--
-- Indices de la tabla `resultado`
--
ALTER TABLE `resultado`
  ADD PRIMARY KEY (`idResultado`), ADD KEY `idTest` (`testResultado`), ADD KEY `idEst` (`estResultado`);

--
-- Indices de la tabla `rol`
--
ALTER TABLE `rol`
  ADD PRIMARY KEY (`tipo`);

--
-- Indices de la tabla `tema`
--
ALTER TABLE `tema`
  ADD PRIMARY KEY (`idTema`), ADD KEY `id` (`TemUnidEnsen`);

--
-- Indices de la tabla `test`
--
ALTER TABLE `test`
  ADD PRIMARY KEY (`idTest`), ADD KEY `idTema` (`temaTest`), ADD KEY `temaTest` (`temaTest`);

--
-- Indices de la tabla `testemparejamiento`
--
ALTER TABLE `testemparejamiento`
  ADD PRIMARY KEY (`idEntrena`);

--
-- Indices de la tabla `unidadensenianza`
--
ALTER TABLE `unidadensenianza`
  ADD PRIMARY KEY (`id`), ADD KEY `idAdmin` (`adminUnidEnsen`);

--
-- Indices de la tabla `usuario`
--
ALTER TABLE `usuario`
  ADD PRIMARY KEY (`idUsuario`), ADD KEY `tipo` (`rolUsuario`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `resultado`
--
ALTER TABLE `resultado`
  MODIFY `idResultado` int(11) NOT NULL AUTO_INCREMENT;
--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `administrador`
--
ALTER TABLE `administrador`
ADD CONSTRAINT `administrador_ibfk_1` FOREIGN KEY (`usuarioAdmin`) REFERENCES `usuario` (`idUsuario`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Filtros para la tabla `concepto`
--
ALTER TABLE `concepto`
ADD CONSTRAINT `concepto_ibfk_1` FOREIGN KEY (`tema`) REFERENCES `tema` (`idTema`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Filtros para la tabla `estudiante`
--
ALTER TABLE `estudiante`
ADD CONSTRAINT `estudiante_ibfk_1` FOREIGN KEY (`usuarioEst`) REFERENCES `usuario` (`idUsuario`) ON DELETE CASCADE ON UPDATE CASCADE,
ADD CONSTRAINT `estudiante_ibfk_2` FOREIGN KEY (`estUnidEnsen`) REFERENCES `unidadensenianza` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Filtros para la tabla `ficha`
--
ALTER TABLE `ficha`
ADD CONSTRAINT `ficha_ibfk_1` FOREIGN KEY (`temaFicha`) REFERENCES `tema` (`idTema`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Filtros para la tabla `fichainterrogacion`
--
ALTER TABLE `fichainterrogacion`
ADD CONSTRAINT `fichainterrogacion_ibfk_1` FOREIGN KEY (`ficha`) REFERENCES `ficha` (`idFicha`) ON DELETE CASCADE ON UPDATE CASCADE,
ADD CONSTRAINT `fichainterrogacion_ibfk_2` FOREIGN KEY (`interrogacion`) REFERENCES `interrogacion` (`idInt`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Filtros para la tabla `interrogacion`
--
ALTER TABLE `interrogacion`
ADD CONSTRAINT `interrogacion_ibfk_1` FOREIGN KEY (`testInterrogacion`) REFERENCES `testemparejamiento` (`idEntrena`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Filtros para la tabla `item`
--
ALTER TABLE `item`
ADD CONSTRAINT `item_ibfk_1` FOREIGN KEY (`preguntaItem`) REFERENCES `pregunta` (`idPregunta`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Filtros para la tabla `pregunta`
--
ALTER TABLE `pregunta`
ADD CONSTRAINT `pregunta_ibfk_1` FOREIGN KEY (`testPreg`) REFERENCES `test` (`idTest`) ON DELETE CASCADE ON UPDATE CASCADE,
ADD CONSTRAINT `pregunta_ibfk_2` FOREIGN KEY (`conceptoPreg`) REFERENCES `concepto` (`idConcepto`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Filtros para la tabla `resultado`
--
ALTER TABLE `resultado`
ADD CONSTRAINT `resultado_ibfk_1` FOREIGN KEY (`estResultado`) REFERENCES `estudiante` (`idEst`) ON DELETE CASCADE ON UPDATE CASCADE,
ADD CONSTRAINT `resultado_ibfk_2` FOREIGN KEY (`testResultado`) REFERENCES `test` (`idTest`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Filtros para la tabla `tema`
--
ALTER TABLE `tema`
ADD CONSTRAINT `tema_ibfk_1` FOREIGN KEY (`TemUnidEnsen`) REFERENCES `unidadensenianza` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Filtros para la tabla `test`
--
ALTER TABLE `test`
ADD CONSTRAINT `test_ibfk_1` FOREIGN KEY (`temaTest`) REFERENCES `tema` (`idTema`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Filtros para la tabla `unidadensenianza`
--
ALTER TABLE `unidadensenianza`
ADD CONSTRAINT `unidadensenianza_ibfk_1` FOREIGN KEY (`adminUnidEnsen`) REFERENCES `administrador` (`idAdmin`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Filtros para la tabla `usuario`
--
ALTER TABLE `usuario`
ADD CONSTRAINT `usuario_ibfk_1` FOREIGN KEY (`rolUsuario`) REFERENCES `rol` (`tipo`) ON DELETE CASCADE ON UPDATE CASCADE;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
