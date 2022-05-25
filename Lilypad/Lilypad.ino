char val;
int LedPin = 13;
boolean LEDState = LOW;
const int xpin = A2;                  // x-axis of the accelerometer
const int ypin = A4;                  // y-axis
const int zpin = A3;                  // z-axis (only on 3-axis models)
//
int sampleDelay = 500;   //number of milliseconds between readings

void setup() {
  // put your setup code here, to run once:
  pinMode(LedPin, OUTPUT);
  Serial.begin(9600);
  
  pinMode(xpin, INPUT);
  pinMode(ypin, INPUT);
  pinMode(zpin, INPUT);
}

void loop() {
  if(Serial.available() > 0){
    val = Serial.read();
    if(val == '1'){
      LEDState = !LEDState;
      digitalWrite(LedPin, LEDState);
    }
    delay(100);
  }
  int x = analogRead(xpin);
  delay(1); 
  int y = analogRead(ypin);
    delay(1); 
  int z = analogRead(zpin);
  float zero_G =512; 
  float scale =102.3;
  Serial.print(((float)x - zero_G)/scale);
  Serial.print(" ");
  Serial.print(((float)y - zero_G)/scale);
  Serial.print(" ");
  Serial.print(((float)z - zero_G)/scale);
  Serial.print("\n");
  delay(sampleDelay);
}
