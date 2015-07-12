delimiter $$
create trigger trggBeforeInsertUsuario before insert on Usuario FOR EACH ROW
begin
set @ultimoCodigo=(select max(idUsuario) from Usuario);
if @ultimoCodigo is null then
	set @ultimoCodigo="0000000";
end if;
set @parteNumerica=@ultimoCodigo+1;
set @codigo=(@parteNumerica);
set NEW.idUsuario=(select @codigo);
end
$$

