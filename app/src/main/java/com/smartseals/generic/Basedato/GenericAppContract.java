package com.smartseals.generic.Basedato;

public class GenericAppContract {

    public interface BaseColumn {
        String ID = "_id";
        String FECHA_SISTEMA = "fechaSistema";
        String ELIMINADO = "eliminado";
    }

    public interface UsuarioColumn extends BaseColumn {
        String USUARIO_ID = "usuarioId";
        String BRIGADA_ID = "brigadaId";
        String NOMBRE_USUARIO = "nombre";
        String APELLIDO_USUARIO = "apellido";
        String NOMBRE_ROL = "nombreRol";
        String USUARIO = "usuario";
        String NOMBRE_SECTOR = "nombreSector";
        String SIGLA_SECTOR = "siglaSector";
        String SECTOR_ID = "sectorId";
        String ROL_ID = "rolId";
        String EMPRESA = "empresa";
    }



    public interface OrdenDeTrabajoColumn extends BaseColumn {
        String ODT_ID = "odtId";
        String CODIGO_ODT = "codigoOdt";
        String DESCARGO = "descargo";
        String CIRCUITO_ID = "circuitoId";
        String MATRICULA_CIRCUITO = "matriculaCircuito";
        String USUARIO_ID = "usuarioId";
        String FECHA_INICIO_PLANIFICADA = "fechaInicioPlanificada";
        String FECHA_FIN_PLANIFICADA = "fechaFinPlanificada";
        String FECHA_INICIO_REAL = "fechaInicioReal";
        String FECHA_FIN_REAL = "fechaFinReal";
        String ELEMENTO_PLANIFICADO = "elementoPlanificado";
        String SINCRONIZADO = "odtSincronizado";
        String IS_PLANIFICADO = "circuitoPlanificado";
        String IS_PLANIFICADO_SERVIDOR = "circuitoPlanificadoServidor";
        String REPORTADO = "reportado";
        String USUARIO_ID_ASIGANDO = "usuarioIdAsignado";
    }

    public interface DescargoColumn extends BaseColumn {
        String DESCARGO_ID = "descargoId";
        String ODT_ID = "id_odt";
        String FECHA_TRABAJO = "fecha_trabajo";
        String HORA_INICIO = "hora_inicio";
        String HORA_FIN = "hora_fin";
        String CODIGO_DESCARGO = "codigo_descargo";
        String PERSONAL_INSCRITO = "personalInscrito";
        String ELEMENTOS_DE_SEGURIDAD = "elementosDeSeguridad";
        String DESCARGO_FINALIZADO = "descargoFinalizado";
        String USUARIO_ID_ASIGANDO = "usuarioIdAsignado";
        String CONTADOR_GPS = "contadorGps";
    }


    public interface LogColumns extends BaseColumn {
        String TAG = "Tag";
        String EXCEPTION = "Exception";
        String CAUSA = "Causa";
        String MENSAJE = "Mensaje";
        String DEVICEID = "DeviceId";
        String USERNAME = "UserName";
        String VERSION_ANDROID = "AndroidVersion";
        String VERSION_CODE = "VersionCode";
        String VERSION_NAME = "VersionName";
        String GUID = "Guid";
        String SINCRONIZADO = "Sincronizado";
    }
    /*Fin de los nodos para FireBase*/
}