/**
 * Mimics the feature type as seen on https://ui.aceternity.com/components/feature-sections
 * Here a feature is used to describe a core offering of the business
 */
interface FeatureType {
  title: string,
  description: string,
  icon?: React.ReactNode,
  index: number
}