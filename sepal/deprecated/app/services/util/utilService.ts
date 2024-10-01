/**
 * Delays the app for the given number of milliseconds
 *
 * Example usage: await delay(3000);
 *
 * @param milliseconds milliseconds to delay
 */
export function delay(milliseconds: number) {
  return new Promise(resolve => {
    setTimeout(resolve, milliseconds);
  });
}