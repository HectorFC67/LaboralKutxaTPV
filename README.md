# LaboralKutxaTPV
Aplicación TPV de Laboral Kutxa para fomentar la fidelización de nuestros clientes.

## Introducción
Este documento es el manual de usuario para hacer uso de la aplicación TPV creada en este repositorio, para tener presente este es el manual de usuario creado para el primer sprint de la aplicación, lo que se irá actualizando y contará con más funcionalidades que todavía no cuenta.  

En esta guía se verán los siguientes puntos:
 * Inicio y cierre de sesión
 * Realizar una compra
 * Gestionar los productos de la empresa
 * Ver el historial de ventas
## Inicio y registro de usuario
En este punto, se verá el inicio de sesión y cierre de sesión en la aplicación TPV. En esta primera versión, la aplicación no cuenta con un registro de usuario funcional. Por lo que, se inicia sesión en un usuario ficticio, al no haber registro de sesión funcional, tampoco existen los usuarios, así que en este manual de instrucciones, se usará el nombre de _LaboralKutxa_ como usuario, y como contraseña se usará _1234_.  

 1. En la página de inicio de sesión, se agrega en el apartado de _Teléfono, usuario o correo_ el nombre de usuario que se utilizará para este ejemplo, es decir *_LaboralKutxa_*, luego añadimos la contraseña de nuestro usuario inventado, en este caso se puede poner por ejemplo: _1234_.
 
       ![Ventana de inicio de sesión](https://github.com/user-attachments/assets/6c294091-5efb-496b-b6e4-75b834cbee97)

 2. Después de agregar tanto el usuario como la contraseña pulsamos el botón _*Iniciar Sesión*_
 3. Para cerrar sesión, dentro de la misma ventana a la que hemos llegado tras iniciar sesión, es decir la página principal, pulsamos el siguiente icono ![Menu de hamburguesa](https://github.com/user-attachments/assets/964d615c-9ccf-4fe9-8723-6ef2433ad7ce) y dentro del menú desplegable pulsamos el botón de _*Cerrar Sesión*_

      ![Menú desplegable para cerrar sesión](https://github.com/user-attachments/assets/bb67341a-c61d-4ba4-aeb1-d852de7cdab6)

## Realizar una compra
En esta primera versión tenemos dos formas diferentes para realizar una compra, una en la que el importe se añade manualmente y en el otro el importe se calcula en base a los productos seleccionados en la compra.

 1. Dentro de la página principal encontraremos dos formas de realizar una compra, la primera en la que se introduce a mano los productos y el importe se calcula automaticamente, en rojo, y la segunda en la que se introduce el importe a mano, en verde.

     ![Ventana principal para realizar una compra con la aplicación TPV](https://github.com/user-attachments/assets/70fdfba4-56d5-46ac-9cbb-3b54d228aa7d)

 2. Después de pulsar cualquiera de ambos botones encontramos dos opciones diferentes antes de seleccionar el método de pago.

    * Después de pulsar el botón de _*Realizar Compra*_ encontramos esta página para añadir productos, para añadir un producto seleccuiba en el *+* que está en el nombre del producto, para quitar la cantidad del producto en cambio, se tiene que seleccionar el botón de *-* que está en el lateral del producto. Vemos que al añadir productos el _Total a pagar_ va aumentando, después de añadir todos los productos que nos interesen, seleccionas el botón _*Método de pago*_

        ![Selección de productos en la ventana realizar compra](https://github.com/user-attachments/assets/0f70a021-7506-44f2-a706-13d721bb4503)

        ![Importe calculado automaticamente en la ventana realizar compra](https://github.com/user-attachments/assets/53c6c8f7-a557-4f63-9598-b17652530e91)

    * Por el otro lado, si el botón pulsado anteriormente ha sido _Introducir importe_ encontramos una calculadora creada desde cero, donde podemos introducir el importe sin necesidad de añadir los productos a la compra. Después de añadir el importe a mano, pulsamos el botón verde de la calculadora para ir a seleccionar el pago, si queremos deshacer lo escrito pulsamos el botón amarillo y si queremos volver para atrás el botón rojo.

        ![Calculadora de la aplicación TPV](https://github.com/user-attachments/assets/0dbb1260-2802-4799-9bfb-b8dcb350f88d)  
        ![Botones de la calculadora de la aplicación](https://github.com/user-attachments/assets/e07e55a8-fb76-464f-88d2-b7f98c166105)

 3. Ahora en la selección de metodo de pago podemos encontrar tres diferentes metodos de pago para seleccionar: _Efectivo_, _Tarjeta de crédito/débito_ o _Bizum_. En esta primera versión, el método de Bizum no está disponible, pero los otros dos sí. Por otro lado, para realizar una compra por efectivo aparece un lector de código QR para aplicar un cupón de descuento, en esta primera versión creado por nosotros de una manera simple y sin gran funcionalidad añadida. Ahora bien, para realizar la compra con tarjeta pulsamos el botón de _*Tarjeta de crédito/débito*_

    ![Ventana de selección de método de pago](https://github.com/user-attachments/assets/c76cba0f-44fb-4f55-b3a3-c5b3cb556c3f)

 4. En la primera versión no contamos con lector de tarjetas, por lo que tenemos que simularlo, y en esta ventana simulamos el pasar la tarjeta pulsando el botón inferior _*Aceptar*_.

    ![Ventana de pasar la tarjeta por el lector del datáfono](https://github.com/user-attachments/assets/349b56d0-e4a3-45e3-8df4-72ce740f5227)

 5. Después de simular el pasar la tarjeta, también simulamos la identificación de un usuario por la tarjeta y simulamos los cupones con los que cuenta dicho usuario, después de seleccionar el cupón que se desea aplicar, en caso de querer aplicar alguno, pulsamos en el botón inferior _*Aplicar Cupón*_.

    ![Ventana de seleccionar cupones](https://github.com/user-attachments/assets/8ededfed-aa6e-4276-af00-fb346bbd8e5c)


 6. Al aplicar el cupón, se encuentra el resumen de la operación, con el monto inicial, el importe del descuento y el monto final, para pasar a finalizar la compra pulsamos el botón inferior de _*Finalizar Compra*_.

    ![Ventana de resumen de la compra](https://github.com/user-attachments/assets/1cbe2fda-0a49-435d-95bb-7e24e62407db)

 7. Una vez finalizada la compra aparece una encuesta para recabar información de los compradores, por otro lado después de añadir una puntuación del 1 a 5 estrellas, siendo 5 lo mejor, de lo satisfactoria que ha sido la compra. Además, aparecerá una pregunta de _Sí o no_ la cual, será personalizada dependiendo de la compra, aunque en esta primera versión siempre es la misma pregunta.  

    Tras dar el feedback, encontramos la ventana que informa que la compra ha finalizado correctamente y una barra de progreso en la cual se muestra cuanto importe faltaría para obtener un nuevo cupón, o al contrario, un mensaje que indica que se ha obtenido un nuevo cupón.

    ![Ventana emergente para enviar feedback](https://github.com/user-attachments/assets/5b40afde-4f7f-4251-acc6-9fb1a2c8c28e)

    ![Ventana de finalización de la compra](https://github.com/user-attachments/assets/54b10927-9a46-477a-bc74-929656daa253)

## Gestión de productos
Para esta primera versión, se cuenta con una gestión de productos en local, donde contamos con la edición, eliminación y creación de nuevos productos. Además de contar con un filtro de busqueda para buscar los productos.

### Busqueda de productos
Para buscar un producto, ponemos el nombre del producto que deseemos en el filtro de _Buscar productos_.

  ![Filtrado de productos en la ventana de gestión de productos](https://github.com/user-attachments/assets/a9c77133-f451-4136-9761-c5b74b65f5cf)

### Creación de producto
Para la creación de nuevos productos pulsamos el simbolo de *+* que se puede encontrar en la esquina inferior derecha, para añadir un producto se tiene que rellenar los campos del nombre, el precio y el stock disponible. Por otro lado, contamos con una descripción opcional del producto añadido. Para añadirlo tenemos que pulsar el botón de *_Guardar_*

  ![Ventana emergente para la creación de un nuevo producto](https://github.com/user-attachments/assets/683df7a4-2f9f-46d1-8c34-ddbe6c0d7bfb)

### Eliminación de producto
Para la eliminación de un producto ya añadido tenemos que pulsar el simbolo de la papelera que cuenta cada producto de la lista de _Gestión de Productos_, después de seleccionar la papelera, nos saldrá una ventana emergente donde se pregunta si estamos seguros de eliminar el producto que hemos seleccionado, y para borrarlo pulsamos en _*Eliminar*_.

  ![Ventana emergente para la eliminación de productos](https://github.com/user-attachments/assets/9798be8d-5226-491a-a6bb-4222098bb3d8)

### Edición de producto
Para la edición de un producto ya añadido tenemos que pulsar el simbolo del lapiz que cuenta cada producto de la lista de _Gestión de Productos_, después de seleccionar el lapiz, nos saldrá una ventana emergente donde se puede editar los campos con los que cuenta un producto, es similar a la ventana emergente de añadir un nuevo producto, por lo que al pulsar el botón _*Guardar*_ editamos el producto.

  ![Ventana emergente para la edición de productos](https://github.com/user-attachments/assets/b8fe906e-1c68-4c95-b239-623f4cc35a00)

## Historial de Ventas
Por último, la primera versión de la aplicación cuenta con un historial de ventas, en dicha ventana podemos filtrar por los diferentes métodos de pago que hemos mencionado antes, o si deseamos podemos filtrar por todas las ventas, como podemos ver aparece la venta realizada al principio del manual de usuario.

  ![Ventana de historial de ventas](https://github.com/user-attachments/assets/8d5b2d4c-ab83-4da0-9904-985cb18f0269)

También podemos ver dentro de cada compra los productos comprados, el cupón utilizado y la fecha y hora de la realización del pago, para ver más detalle de la compra, se pulsa la flecha que está mirando hacia abajo justo en la parte inferior de cada compra.

  ![Compra más detallada dentro de la ventana de historial de compras](https://github.com/user-attachments/assets/66559725-b3d1-4191-89bb-c7805ef26a42)
  
